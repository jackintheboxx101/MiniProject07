package com.model2.mvc.web.product;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;

@Controller
@RequestMapping("/product/*")
public class ProductController {

	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	public ProductController() {
		System.out.println(this.getClass());
	}
	
	@Value("#{commonProperties['pageUnit']}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	int pageSize;
	

	
//	@RequestMapping(value = "addProduct", method = RequestMethod.GET)
//	public String addProduct() throws Exception{
//		System.out.println("addProduct === GET");
//		
//		return "forward:/product/addProduct.jsp";
//	}
	
	@RequestMapping(value = "addProduct", method = RequestMethod.POST)
	public String addProduct(@ModelAttribute("product") Product product) throws Exception{
		
		System.out.println("addProduct === POST");
		
		productService.addProduct(product);
		
		return "forward:/product/addProduct.jsp";
	}
	
	@RequestMapping(value = "getProduct", method = RequestMethod.POST)
	public String getProduct( @RequestParam("prodNo") int prodNo , Model model, HttpServletRequest request ) throws Exception {
		
		System.out.println("getProduct === POST");
		//Business Logic
		Product product = productService.getProduct(prodNo);
		// Model �� View ����
		model.addAttribute("product", product);
		
			return "redirect:/product/updateProductView?prodNo="+product.getProdNo()+"&menu=manage";
			//return "forward:/product/updateProduct.jsp";
	}
	
	@RequestMapping(value = "getProduct", method = RequestMethod.GET)
	public String getProduct(@RequestParam("prodNo") int prodNo , Model model) throws Exception {
		
		System.out.println("getProduct === GET");
		
		Product product = productService.getProduct(prodNo);
		
		model.addAttribute("product", product);
		
		return "forward:/product/getProduct.jsp";
		//return "forward:/product/updateProduct.jsp";
		
	}
	@RequestMapping(value = "updateProduct", method = RequestMethod.GET )
	public String updateProduct( @ModelAttribute("product") int prodNo , Model model) throws Exception{

		System.out.println("updateProduct === GET");
		//Business Logic
		
		Product product = productService.getProduct(prodNo);
		
		model.addAttribute("product", product);
		
		
		//System.out.println("updateProd==> req ::: "+request);
		
		
		
//		int reqProdNo = Integer.parseInt(request.getParameter("prodNo"));
//		//System.out.println("reqProdNo===>"+reqProdNo);
//		if(reqProdNo == product.getProdNo()){
//			session.setAttribute("product", product);
//			//System.out.println("updateProd.do====="+reqProdNo);
//		}
//		
		return "forward:/product/updateProduct.jsp";
	}

	
	@RequestMapping(value = "updateProduct", method = RequestMethod.POST )
	public String updateProduct( @ModelAttribute("product") Product product , Model model , HttpServletRequest request, HttpSession session) throws Exception{

		System.out.println("updateProduct === POST");
		//Business Logic
		
		productService.updateProduct(product);
		
		//System.out.println("updateProd==> req ::: "+request);
		
		int reqProdNo = Integer.parseInt(request.getParameter("prodNo"));
		//System.out.println("reqProdNo===>"+reqProdNo);
		if(reqProdNo == product.getProdNo()){
			session.setAttribute("product", product);
			//System.out.println("updateProd.do====="+reqProdNo);
		}
		
		return "redirect:/product/getProduct?prodNo="+reqProdNo+"&menu=ok";
	}
	
	@RequestMapping(value = "updateProductView", method = RequestMethod.POST)
	public String updateProductView( @RequestParam("prodNo") int prodNo , Model model ) throws Exception{

		System.out.println("updateProductView === POST");
		
		//Business Logic
		Product product = productService.getProduct(prodNo);
		// Model �� View ����
		model.addAttribute("product", product);
		System.out.println(product);
		return "forward:/product/updateProduct.jsp";
	}
	
	@RequestMapping(value = "listProduct")
	public String listUser( @ModelAttribute("search") Search search , Model model , HttpServletRequest request) throws Exception{
		
		System.out.println("/product/listProduct");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		// Business logic ����
		Map<String , Object> map=productService.getProductList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		// Model �� View ����
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		
		
		return "forward:/product/listProduct.jsp";
	}
}
