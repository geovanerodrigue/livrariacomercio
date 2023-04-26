package livrariacomercio.web.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import livrariacomercio.web.model.Produto;
import livrariacomercio.web.repository.ProdutoRepository;

@Controller
public class ProdutoController {

	private static String caminhoImagens = "E:\\spring-project\\livrariacomercio\\src\\main\\resources\\static\\imagens\\";

	@Autowired
	private ProdutoRepository produtoRepository;

	@RequestMapping(method = RequestMethod.GET, value ="/cadastroproduto")
	public ModelAndView inicio() {
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastroproduto");
		modelAndView.addObject("produtoobj", new Produto());
		return modelAndView;
	}

	@RequestMapping(method=RequestMethod.POST, value="/salvarproduto")
	public ModelAndView  salvar(Produto produto, @RequestParam("file") MultipartFile arquivo) {

		produtoRepository.save(produto);

		try {
			if(!arquivo.isEmpty()) {
				byte[] bytes = arquivo.getBytes();
				Path caminho = Paths.get(caminhoImagens+String.valueOf(produto.getId())+arquivo.getOriginalFilename());
				Files.write(caminho, bytes);

				produto.setNomeimagem(String.valueOf(produto.getId())+arquivo.getOriginalFilename());
				produtoRepository.save(produto);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}

		ModelAndView andView = new ModelAndView("cadastro/cadastroproduto");
		Iterable<Produto> produtosIT = produtoRepository.findAll();
		andView.addObject("produto",produtosIT);
		andView.addObject("produtoobj", new Produto());

		return  andView;
	}

	@RequestMapping(method = RequestMethod.GET, value ="/listaproduto")
	public ModelAndView produto() {
		ModelAndView andView = new ModelAndView("cadastro/cadastroproduto");
		Iterable<Produto> produtosIT = produtoRepository.findAll();
		andView.addObject("produto",produtosIT);
		andView.addObject("produtoobj", new Produto());
		return andView;
	}

	@GetMapping("/editarproduto/{idproduto}")
	public ModelAndView editar(@PathVariable("idproduto")Long idproduto) {

		Optional<Produto> produto = produtoRepository.findById(idproduto);

		ModelAndView modelAndView = new ModelAndView("cadastro/cadastroproduto");
		modelAndView.addObject("produtoobj", produto.get());
		return modelAndView;

	}

   @GetMapping("/removerproduto/{idproduto}")
   public ModelAndView excluir(@PathVariable("idproduto")Long idproduto) {

	  produtoRepository.deleteById(idproduto);

	   ModelAndView modelAndView = new ModelAndView("cadastro/cadastroproduto");
	   modelAndView.addObject("produto", produtoRepository.findAll());
	   modelAndView.addObject("produtoobj", new Produto());
	   return modelAndView;

   }

   @GetMapping("/mostrarimagem/{imagem}")
   @ResponseBody
   public byte [] retornarimagem(@PathVariable("imagem") String imagem) throws IOException {
	File imagemarquivo = new File(caminhoImagens+imagem);
    if(imagem!=null || imagem.trim().length()>0) {
	   return Files.readAllBytes(imagemarquivo.toPath());
    }
	return null;
   }

 }
