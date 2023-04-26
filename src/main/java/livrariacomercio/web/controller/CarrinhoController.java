package livrariacomercio.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import livrariacomercio.web.model.Compra;
import livrariacomercio.web.model.ItensCompra;
import livrariacomercio.web.model.Produto;
import livrariacomercio.web.model.Usuario;
import livrariacomercio.web.repository.CompraRepository;
import livrariacomercio.web.repository.ItensCompraRepository;
import livrariacomercio.web.repository.ProdutoRepository;
import livrariacomercio.web.repository.UsuarioRepository;

@Controller
public class CarrinhoController {

	@Autowired
	private ProdutoRepository repositorioProduto;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private CompraRepository compraRepository;

	@Autowired
	private ItensCompraRepository itensCompraRepository;

	private List<ItensCompra> itensCompra = new ArrayList<>();
	private Compra compra = new Compra();
	private Usuario usuario;

	private void calcularTotal() {
		compra.setValorTotal(0.);
		for(ItensCompra it: itensCompra) {
			compra.setValorTotal(compra.getValorTotal()+it.getValorTotal());
		}
	}

	//att: o problema é que so aceita nome me login iguais, corrigir
	//provavel erro com autenticacao do usuario para finalizar compra
	private void buscarUsuarioLogado() {
	  Authentication autenticado = SecurityContextHolder.getContext().getAuthentication();
	  if(!(autenticado instanceof AnonymousAuthenticationToken)) { //condicao problema
		  String nome = autenticado.getName();
		  usuario = usuarioRepository.buscarUsuarioNome(nome).get(0);
	  }
	}

	@GetMapping("/carrinho")
	public ModelAndView chamarCarrinho() {
		ModelAndView mv = new ModelAndView("carrinho");
		calcularTotal();
		mv.addObject("compra", compra);
		mv.addObject("listaItens", itensCompra);
		return mv;
	}

	@GetMapping("/finalizar")
	public ModelAndView finalizarCompra() {
		buscarUsuarioLogado();
		ModelAndView mv = new ModelAndView("finalizar");
		calcularTotal();
		mv.addObject("compra", compra);
		mv.addObject("listaItens", itensCompra);
		mv.addObject("usuario", usuario);
		return mv;
	}

	@PostMapping("/finalizar/confirmar")
	public ModelAndView confirmarCompra(String formaPagamento) {
	   ModelAndView mv = new ModelAndView("mensagemfinalizou");
	   compra.setUsuario(usuario);
	   compra.setFormaPagamento(formaPagamento);
	   
	   if (itensCompra.isEmpty()) {
		   mv.addObject("mensagem", "Não é possível confirmar a compra com o carrinho vazio.");
		   return mv;
		   }
	   
	   compraRepository.save(compra);

	   for(ItensCompra c:itensCompra) {
		   c.setCompra(compra);
		   itensCompraRepository.save(c);
	   }

	   itensCompra = new ArrayList<>();
	   compra = new Compra();
	   return mv;
	}

	@GetMapping("/alterarQuantidade/{id}/{acao}")
	public String alterarQuantidade(@PathVariable Long id, @PathVariable Integer acao) {
		for(ItensCompra it:itensCompra) {
			if(it.getProduto().getId().equals(id)) {
				if(acao.equals(1)) {
				it.setQuantidade(it.getQuantidade()+1);
				it.setValorTotal(0.);
				it.setValorTotal(it.getValorTotal()+(it.getQuantidade()*it.getValorUnitario()));
				}else if(acao==0) {
			    it.setQuantidade(it.getQuantidade()-1);
			    it.setValorTotal(0.);
			    it.setValorTotal(it.getValorTotal()+(it.getQuantidade()*it.getValorUnitario()));
				}
				break;
			}
		}
		return "redirect:/carrinho";
	}

	@GetMapping("/removerProduto/{id}")
	public String removerProdutoCarrinho(@PathVariable Long id) {
		for(ItensCompra it:itensCompra) {
			if(it.getProduto().getId().equals(id)) {
				itensCompra.remove(it);
				break;
			}
		}
		return "redirect:/carrinho";
	}

	@GetMapping("/adicionarCarrinho/{id}")
	public String adicionarCarrinho(@PathVariable Long id) {
		Optional<Produto> prod = repositorioProduto.findById(id);
		Produto produto = prod.get();

		int controle = 0;
		for(ItensCompra it:itensCompra) {
			if(it.getProduto().getId().equals(produto.getId())) {
				it.setQuantidade(it.getQuantidade()+1);
				it.setValorTotal(0.);
				it.setValorTotal(it.getValorTotal()+(it.getQuantidade()*it.getValorUnitario()));
				controle = 1;
				break;
			}
		}
		if(controle==0) {
		ItensCompra item = new ItensCompra();
		item.setProduto(produto);
		item.setValorUnitario(produto.getPreco());
		item.setQuantidade(item.getQuantidade()+1);
		item.setValorTotal(item.getValorTotal()+(item.getQuantidade()*item.getValorUnitario()));
		itensCompra.add(item);
		}
		return "redirect:/carrinho";
	}
}
