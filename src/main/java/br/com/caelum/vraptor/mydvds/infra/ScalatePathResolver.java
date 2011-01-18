package br.com.caelum.vraptor.mydvds.infra;

import java.io.File;

import javax.servlet.ServletContext;

import br.com.caelum.vraptor.http.FormatResolver;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.com.caelum.vraptor.validator.Validations;
import br.com.caelum.vraptor.view.DefaultPathResolver;

@Component
@RequestScoped
public class ScalatePathResolver extends DefaultPathResolver {

	private final ServletContext context;
	
	
	public ScalatePathResolver(FormatResolver resolver,ServletContext context) {
		super(resolver);
		System.out.println("iniciando meu path resolver");
		this.context = context;
	}


	protected String getPrefix() {	
		return "/WEB-INF/ssp/";
	}

	protected String getExtension() {
		return "ssp";
	}

	@Override
	public String pathFor(ResourceMethod method) {
		new Validations();
		String path = super.pathFor(method);			
		String realPathToViewFile = context.getRealPath(path);
		return new File(realPathToViewFile).exists() ? path : path.replace(
				"/WEB-INF/ssp", "/WEB-INF/jsp").replace(".ssp", ".jsp");
	}

}
