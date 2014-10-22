package com.claudesoft.FileServer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import com.claudesoft.FileServer.FileEntity;
import com.google.gson.Gson;

/**
 * Servlet implementation class FileUpload
 */
@WebServlet("/FileUpload")
public class FileUpload extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileUpload() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		OutputStream out = response.getOutputStream();
		String[] fileExts={"doc","zip","rar","jpg","txt"};  
		  //创建一个临时文件存放要上传的文件，第一个参数为上传文件大小，第二个参数为存放的临时目录  
		    DiskFileItemFactory factory = new DiskFileItemFactory(1024*1024*5,new File("\temp1"));  
		    // 设置缓冲区大小为 5M  
		    factory.setSizeThreshold(1024 * 1024 * 5);  
		    // 创建一个文件上传的句柄  
		    ServletFileUpload upload = new ServletFileUpload(factory);  

		    //设置上传文件的整个大小和上传的单个文件大小  
		    upload.setSizeMax(1024*1024*50);  
		    upload.setFileSizeMax(1024*1024*5);  

		    try {  
		        //把页面表单中的每一个表单元素解析成一个FileItem  
		        List<FileItem> items = upload.parseRequest(request);
		        List<FileEntity> files = new ArrayList<FileEntity>();
		        for (FileItem fileItem : items) {  
		            //如果是一个普通的表单元素(type不是file的表单元素)  
		            if(fileItem.isFormField()){  
		                System.out.println(fileItem.getFieldName());  //得到对应表单元素的名字  
		                System.out.println(fileItem.getString());  //得到表单元素的值  
		            }else{  
		                //获取文件的后缀名  
		                String fileName = fileItem.getName();//得到文件的名字  
		                String fileExt = fileName.substring(fileName.lastIndexOf(".")+1, fileName.length());  

		                if(Arrays.binarySearch(fileExts, fileExt)!=-1){  
		                    try {  
		                        //将文件上传到项目的upload目录并命名，getRealPath可以得到该web项目下包含/upload的绝对路径  
//		                        fileItem.write(new File(request.getServletContext().getRealPath("/upload")+"/"  
//		                                + UUID.randomUUID().toString()+"."+fileExt));  
		                        //fileItem.write(new File("D:/test2.png"));  
//		                        logger.info("文件上传路径："+request.getServletContext().getRealPath("/upload")+"/"  
//		                                + UUID.randomUUID().toString()+"."+fileExt);  
		                    	
		                    	String basePath = request.getServletContext().getRealPath("/upload");
		                    	String jid = request.getParameter("JID");
		                    	File fileDir =new File(basePath+"/"+jid);
		                    	long currentTime=System.currentTimeMillis();  
		                    	//如果文件夹不存在则创建    
		                    	if  (!fileDir .exists()  && !fileDir .isDirectory())      
		                    	{       
		                    		fileDir .mkdirs();   
		                    		
		                    	} 
		                    	fileItem.write(new File(fileDir.getPath()+"/"+currentTime+"."+fileExt));
 
		                    	
		                    	
		                    	
		                    	FileEntity file = new FileEntity();
		                    	file.fileName = String.valueOf(currentTime);
		                    	file.fileSize = fileItem.getSize();
		                    	file.fileExt = fileExt;
		                    	files.add(file);
		                    	
		                    	System.out.println("文件上传成功");
		                    	
		                    	

		                    } catch (Exception e) {  
		                        e.printStackTrace();  
		                    }  
		                }else{  
		                    System.out.println("the File type Can't Upload");  
		                }  
		            }  
		        }  
		        if(files.size()>0){
		        	
		        	Gson gson = new Gson();
		        	
		        	out.write(gson.toJson(files).getBytes("UTF-8"));
		        }
		    } catch (FileUploadBase.SizeLimitExceededException e) {  
		        System.out.println("All request UploadFile Max");  
		    } catch (FileUploadBase.FileSizeLimitExceededException e) {  
		        System.out.println("A request UploadFile Max");  
		    }catch (FileUploadException e) {  
		        e.printStackTrace();  
		    }  
	}

}
