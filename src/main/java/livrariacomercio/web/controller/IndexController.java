package livrariacomercio.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import livrariacomercio.web.model.Produto;
import livrariacomercio.web.repository.ProdutoRepository;

@Controller
public class IndexController {

	private static String caminhoImagens = "E:\\spring-project\\livrariacomercio\\src\\main\\resources\\static\\imagens\\";

	@Autowired
	private ProdutoRepository produtoRepository;

	@RequestMapping(method = RequestMethod.GET, value ="/")
	public ModelAndView listaproduto() {
		ModelAndView andView = new ModelAndView("index");
		Iterable<Produto> produtosIT = produtoRepository.findAll();
		andView.addObject("produto",produtosIT);
		andView.addObject("produtoobj", new Produto());
		return andView;
	}

	@PostMapping("**/pesquisaproduto")
	public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomepesquisa) {
		ModelAndView modelAndView = new ModelAndView("index");
		modelAndView.addObject("produto", produtoRepository.finfProdutoByName(nomepesquisa));
		modelAndView.addObject("produtoobj", new Produto());
		return modelAndView;
	}

	@RequestMapping(method = RequestMethod.GET, value ="/adminarea")
	public ModelAndView adminarea() {
		ModelAndView andView = new ModelAndView("cadastro/adminarea");
		return andView;
	}

}
