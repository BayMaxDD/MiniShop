package com.briup.web.servlet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.briup.domain.Category;
import com.briup.domain.Product;
import com.briup.service.ProductService;
import com.briup.util.CommonsUtil;

/**
 * Servlet implementation class AdminAddProductServlet
 */
public class AdminAddProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminAddProductServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		//目的：收集表单的数据 封装一个Product实体 将上传图片存到服务器磁盘上
		
		//收集数据的容器
		Map<String, Object> map =
				new HashMap<String,Object>();
		Product product = new Product();
		try {
			//创建磁盘文件项工厂
			DiskFileItemFactory factory = 
					new DiskFileItemFactory();
			//创建文件上传核心对象
			ServletFileUpload upload =
					new ServletFileUpload(factory);
			//解析文件项,获得文件项集合
			List<FileItem> parseRequest =
					upload.parseRequest(request);
			if(parseRequest != null){
				for (FileItem item : parseRequest) {
					boolean formField = item.isFormField();
					if(formField){
						//是普通表单项
						String fieldName = item.getFieldName();
						String filedValue = item.getString("UTF-8");
						
						map.put(fieldName, filedValue);
					} else{
						//文件上传项
						//获得文件名字
						String filename = item.getName();
						String finalName = null;
						
						if(!"".equals(filename)){
							//文件存在,进行名字改造,防止重名
							String postfix = 
									filename.substring(filename.lastIndexOf("."),
													   filename.length());
							finalName = String.valueOf(System.currentTimeMillis()) + postfix;
							//获得内容
							InputStream in = item.getInputStream();
							//获得上传位置
							String path =
									this.getServletContext().getRealPath("upload");
							OutputStream out =
									new FileOutputStream(path+"/"+finalName);
							IOUtils.copy(in, out);
							in.close();
							out.close();
							
							//对于pimage项,将地址存放到数据库中
							map.put("pimage", "upload/"+finalName);
						} else {
							//文件不存在
							finalName = "NotExistPic.jpg";
							//对于pimage项,将地址存放到数据库中
							map.put("pimage", "products/"+finalName);
						}
						//删除临时文件
						item.delete();
					}
				}
			}
			BeanUtils.populate(product,map);
			//封装pid
			product.setPid(CommonsUtil.getUUid());
			//封装日期
			product.setPdate(new Date());
			//是否下架
			product.setPflag(0);
			//类别
			Category category = new Category();
			category.setCid(map.get("cid").toString());
			product.setCategory(category);
			
			//添加Product
			ProductService service =
					new ProductService();
			service.addProduct(product);
			
			response.sendRedirect(request.getContextPath()+"/AdminServlet?method=productList");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
