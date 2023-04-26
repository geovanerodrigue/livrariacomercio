package livrariacomercio.web.controller;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import livrariacomercio.web.model.Telefone;
import livrariacomercio.web.model.Usuario;
import livrariacomercio.web.repository.UsuarioRepository;



@Controller
public class UsuarioController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	private Usuario usuario;

	@RequestMapping(method = RequestMethod.GET, value ="/cadastrousuario")
	public ModelAndView inicio() {
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastrousuario");
		modelAndView.addObject("usuarioobj", new Usuario());
		return modelAndView;
	}

	@RequestMapping(method=RequestMethod.POST, value="/salvarusuario")
	public ModelAndView  salvar(Usuario usuario) throws Exception {

		for (Telefone element : usuario.getTelefones()) {
			element.setUsuario(usuario);
		}

		URL url = new URL("HTTPS://viacep.com.br/ws/"+usuario.getCep()+"/json/ ");

		URLConnection connection = url.openConnection();

		InputStream is = connection.getInputStream();

		BufferedReader  br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

		String cep = "";
		StringBuilder jsonCep = new StringBuilder();

		while((cep = br.readLine()) != null) {
			jsonCep.append(cep);
		}

		Usuario userAux = new Gson().fromJson(jsonCep.toString(), Usuario.class);

		usuario.setCep(userAux.getCep());

		usuario.setLogradouro(userAux.getLogradouro());

		usuario.setComplemento(userAux.getComplemento());

		usuario.setBairro(userAux.getBairro());

		usuario.setLocalidade(userAux.getLocalidade());

		usuario.setUf(userAux.getUf());

		String senhacriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(senhacriptografada);
		usuarioRepository.save(usuario);

		ModelAndView andView = new ModelAndView("cadastro/cadastrousuario");
		Iterable<Usuario> pessoasIT = usuarioRepository.findAll();
		andView.addObject("usuario",pessoasIT);
		andView.addObject("usuarioobj", new Usuario());

		return  andView;
	}

	private void buscarUsuarioLogado() {
		  Authentication autenticado = SecurityContextHolder.getContext().getAuthentication();
		  if(!(autenticado instanceof AnonymousAuthenticationToken)) { 
			  String nome = autenticado.getName();
			  usuario = usuarioRepository.buscarUsuarioNome(nome).get(0);
		  }
		}


	@GetMapping("/meucadastro")
	public ModelAndView listacadastro() {
		ModelAndView andView = new ModelAndView("cadastro/meucadastro");
		buscarUsuarioLogado();
		andView.addObject("usuario", usuario);
		return andView;
	}

	@GetMapping("/editarusuario/{idusuario}")
	public ModelAndView editarusuario(@PathVariable("idusuario")Long idusuario) {

		Optional<Usuario> usuario = usuarioRepository.findById(idusuario);

		ModelAndView modelAndView = new ModelAndView("cadastro/meucadastroeditar");
		modelAndView.addObject("usuarioobj", usuario.get());
		modelAndView.addObject("usuario", usuarioRepository.findById(idusuario));
		return modelAndView;

	}

	@RequestMapping(method=RequestMethod.POST, value="/editarcadastro")
    public ModelAndView editar(Usuario usuario) throws Exception{
        ModelAndView mv =  new ModelAndView("cadastro/meucadastroeditar");
        
        
        URL url = new URL("HTTPS://viacep.com.br/ws/"+usuario.getCep()+"/json/ ");

        URLConnection connection = url.openConnection();

        InputStream is = connection.getInputStream();

        BufferedReader  br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

        String cep = "";
        StringBuilder jsonCep = new StringBuilder();

        while((cep = br.readLine()) != null) {
            jsonCep.append(cep);
        }

        Usuario userAux = new Gson().fromJson(jsonCep.toString(), Usuario.class);

        usuario.setCep(userAux.getCep());

        usuario.setLogradouro(userAux.getLogradouro());

        usuario.setBairro(userAux.getBairro());

        usuario.setLocalidade(userAux.getLocalidade());

        usuario.setUf(userAux.getUf());
        
        Usuario usuarioExistente = usuarioRepository.findById(usuario.getId()).orElseThrow(() -> new IllegalArgumentException("ID de usuário inválido: " + usuario.getId()));
        usuario.setSenha(usuarioExistente.getSenha());
        return listacadastro();
        
    }
	
	@GetMapping("/editarsenha/{idusuario}")
	public ModelAndView editarsenha(@PathVariable("idusuario")Long idusuario) {

		Optional<Usuario> usuario = usuarioRepository.findById(idusuario);

		ModelAndView modelAndView = new ModelAndView("cadastro/minhasenhaeditar");
		modelAndView.addObject("usuarioobj", usuario.get());
		modelAndView.addObject("usuario", usuarioRepository.findById(idusuario));
		return modelAndView;

	}

	@RequestMapping(method=RequestMethod.POST, value="/editarsenha")
    public ModelAndView editarsenha(Usuario usuario) throws Exception{
        ModelAndView mv =  new ModelAndView("cadastro/minhasenhaeditar");
        
        
        URL url = new URL("HTTPS://viacep.com.br/ws/"+usuario.getCep()+"/json/ ");

        URLConnection connection = url.openConnection();

        InputStream is = connection.getInputStream();

        BufferedReader  br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

        String cep = "";
        StringBuilder jsonCep = new StringBuilder();

        while((cep = br.readLine()) != null) {
            jsonCep.append(cep);
        }

        Usuario userAux = new Gson().fromJson(jsonCep.toString(), Usuario.class);

        usuario.setCep(userAux.getCep());

        usuario.setLogradouro(userAux.getLogradouro());

        usuario.setBairro(userAux.getBairro());

        usuario.setLocalidade(userAux.getLocalidade());

        usuario.setUf(userAux.getUf());
        
		String senhacriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(senhacriptografada);
		
        return listacadastro();
        
    }

}
