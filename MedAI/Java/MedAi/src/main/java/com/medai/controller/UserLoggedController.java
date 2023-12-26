package com.medai.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.net.http.HttpResponse;
import java.security.Principal;
import java.util.Map;

import org.apache.tomcat.util.file.ConfigurationSource.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.medai.entities.User;
import com.medai.repo.UserRepo;
import com.medai.helper.Message;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserLoggedController {
	
	private static final String EXTERNAL_FILE_PATH = "C:/MedAi/src/main/resources/static/";
	@Autowired
	private UserRepo repo;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@GetMapping("/home")
	public String userHome(Model model, Principal p) {
		String name = p.getName();
		User user = repo.getUserByUsername(name);
		model.addAttribute("user",user);
		model.addAttribute("breastCancer",user.getBreastCancerResult());
		return "/user/userhome";
	}
	
	@GetMapping("/dashboard")
	public String dashboard(Model model, Principal p) {
		String name = p.getName();
		User user = repo.getUserByUsername(name);
		model.addAttribute("user",user);
		model.addAttribute("breastCancer",user.getBreastCancerResult());
		return "user/userdash";
	}
	@GetMapping("/breast-cancer-prediction")
	public String getFormBreastInput(Model model, Principal p) {
		String name = p.getName();
		User user = repo.getUserByUsername(name);
		model.addAttribute("user",user);
		return "/user/breastprediction";
	}
	
	@RequestMapping(value = "/send_data_to_prediction_model", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String sendDataToPredictionModel(@RequestParam Map<String, String> inputData, Model model, Principal p, HttpSession session ) {
        String predictionApiUrl = "http://localhost:8000/predict";
        String name = p.getName();
        User user = repo.getUserByUsername(name);
        // Convert the Map to MultiValueMap for form data
        MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();
        for (Map.Entry<String, String> entry : inputData.entrySet()) {
            requestMap.add(entry.getKey(), entry.getValue());
        }
        System.out.println(inputData);
        // Set the headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Create the HTTP entity
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestMap, headers);

        // Make the request
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(predictionApiUrl, requestEntity, String.class);
        model.addAttribute("user",user);
        String responseBody = responseEntity.getBody();
        String responseBodyO = responseBody.replaceAll("\"", "");
        System.out.println(responseBodyO);
        user.setBreastCancerResult(responseBodyO);
        repo.save(user);
        model.addAttribute("result", user.getBreastCancerResult());
        session.setAttribute("message", new Message(user.getBreastCancerResult()));
        return "redirect:/user/dashboard";
	}
	
	@GetMapping("/diabetes-prediction")
	public String getDiabetesForm(Model model, Principal p) {
		String name = p.getName();
		User user = repo.getUserByUsername(name);
		model.addAttribute("user", user);
		return "/user/diabetes";
	}
	
	@RequestMapping(value = "/send_data_to_diabetes_prediction_model", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String sendDataToDiabetesPredictionModel(@RequestParam Map<String, String> inputData, Model model, Principal p, HttpSession session ) {
        String predictionApiUrl = "http://localhost:8181/predict";
        String name = p.getName();
        User user = repo.getUserByUsername(name);
        // Convert the Map to MultiValueMap for form data
        MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();
        for (Map.Entry<String, String> entry : inputData.entrySet()) {
            requestMap.add(entry.getKey(), entry.getValue());
        }
        System.out.println(inputData);
        // Set the headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Create the HTTP entity
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestMap, headers);

        // Make the request
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(predictionApiUrl, requestEntity, String.class);
        model.addAttribute("user",user);
        String responseBody = responseEntity.getBody();
        user.setDiabetesResult(responseBody.replaceAll("\"", ""));
        repo.save(user);
        model.addAttribute("result", user.getDiabetesResult());
        session.setAttribute("message", new Message(user.getDiabetesResult()));
        return "redirect:/user/dashboard";
	}
	
	@GetMapping("/brain-tumor-prediction")
	public String getBrainTumorForm(Model model, Principal p) {
		String name = p.getName();
		User user = repo.getUserByUsername(name);
		model.addAttribute("user", user);
		return "/user/braintumor";
	}
	@GetMapping("/chat-doctor")
	public String getChatPage(Model model, Principal p) {
		String name = p.getName();
		User user = repo.getUserByUsername(name);
		model.addAttribute("user", user);
		return "/user/doctor_talk";
	}
	
	//download diet plan]
	@GetMapping("/download")
	public void getDownLoadFilename(HttpServletResponse response) throws IOException {
		File file = new File("download\\Diabetic Diet Plan.pdf");
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
		
		ServletOutputStream outputStream = response.getOutputStream();
		BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
		byte[] buffer = new byte[8192];
		int byteReads = -1;
		while((byteReads = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer,0,byteReads);
		}
		inputStream.close();
		outputStream.close();
		
	}
}
















