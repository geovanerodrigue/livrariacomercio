package livrariacomercio.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import livrariacomercio.web.model.Compra;
import livrariacomercio.web.model.ItensCompra;
import livrariacomercio.web.model.Usuario;
import livrariacomercio.web.repository.CompraRepository;
import livrariacomercio.web.repository.ItensCompraRepository;
import livrariacomercio.web.repository.UsuarioRepository;

@Controller
public class CompraController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private CompraRepository compraRepository;

	@Autowired
	private ItensCompraRepository itensCompraRepository;

    private ItensCompra itencompra;

	private Usuario usuario;

	private Compra compra;

	private List<ItensCompra> itensCompra = new ArrayList<>();


	@RequestMapping(method = RequestMethod.GET, value ="/compras")
	public ModelAndView inicio() {
		ModelAndView andView = new ModelAndView("compra/compras");
		Iterable<Compra> comprasIT = compraRepository.findAll();
		andView.addObject("compra",comprasIT);
		andView.addObject("compraobj", new Compra());
		return andView;
	}

	private void buscarUsuarioLogado() {
		  Authentication autenticado = SecurityContextHolder.getContext().getAuthentication();
		  if(!(autenticado instanceof AnonymousAuthenticationToken)) { //condicao problema
			  String nome = autenticado.getName();
			  usuario = usuarioRepository.buscarUsuarioNome(nome).get(0);
		  }
		}


	/*
	  //metodo 1 que esta funcional
	@RequestMapping(method = RequestMethod.GET, value ="/comprasinformacoes")
	public ModelAndView comprasinformacoes() {
		ModelAndView andView = new ModelAndView("compra/comprasinformacoes");
		Iterable<ItensCompra> itenscomprasIT = itensCompraRepository.findAll();
		andView.addObject("itenscompra", itenscomprasIT);
		andView.addObject("compraobj", new ItensCompra());
		return andView;
	}


	/*
	 * metodo 2 pra testar
	@GetMapping("/comprasinformacoes/{compra.id}")
	public ModelAndView retornaritemcompra(@PathVariable Long id, Compra compra) {
		ModelAndView andView = new ModelAndView("compra/comprasinformacoes");
		Iterable<ItensCompra> itenscomprasIT = itensCompraRepository.findCompraById(id);
		andView.addObject("itenscompra", itenscomprasIT);
		andView.addObject("compraobj", new ItensCompra());
		return andView;
	}
	*/

	/*
	@GetMapping("/listacompras/{idcompra}")
	public ModelAndView listacomprasitenscompra(@PathVariable("idcompra")Long compra) {
		ModelAndView modelAndView = new ModelAndView("compra/comprasinformacoes");
	    Optional<Compra> compras = compraRepository.findById(compra);
		modelAndView.addObject("compraobj", compras.get());
		modelAndView.addObject("compras", itensCompraRepository.findAllById(compras));
		return modelAndView;

	}
	*/

    @GetMapping("**/comprasinformacoes/{compraid}")
    public String getItensCompra(@PathVariable("compraid") Long compraid,Model model) {
        List<ItensCompra> itensCompra = itensCompraRepository.findByCompraId(compraid);
        model.addAttribute("itenscompra", itensCompra);
        return "compra/comprasinformacoes";
    }


    @GetMapping("minhacompra/{idusuario}")
    public String getUserCompra(@PathVariable("idusuario") Long idusuario,Model model) {
        List<Compra> compra = compraRepository.findByUsuarioId(idusuario);
        model.addAttribute("compra", compra);
        return "compra/minhacompra";
    }

}
