package com.pl_visualization_project.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
//import org.json.JSONObject;

import java.util.ArrayList;
//import java.util.Vector; 
import cycuice.sourceAnalyzer.SourceAnalyzer;
import cycuice.sourceAnalyzer.AnalysisResult;


@Controller     // 表示當下的java是一個Controller
public class DemoController {

    @GetMapping("/")
    public String showForm() {
        return "home"; // return表單頁面的模板名稱
    }

    @PostMapping("/set-cookie")
    public ResponseEntity<String> setCookie(HttpServletRequest request, HttpServletResponse response, @RequestParam String text) {

        HttpSession session = request.getSession();
        SourceAnalyzer ourScheme = null;
        ArrayList<AnalysisResult> result_list = null;

        if ( session.isNew() ) {
            ourScheme = new SourceAnalyzer();
            result_list = ourScheme.analyze(text);
            // session.setAttribute("ourScheme_obj", ourScheme);     // 實際上 Spring Session 會提供一個它自己的 Session 實現，並且會自動將 session 數據保存到 Redis 中
            // session.setMaxInactiveInterval(10*60);       // 三分鐘
        } // if
        else {
            // ourScheme = (SourceAnalyzer)session.getAttribute("user");
            // result_list = ourScheme.analyze(text);
            // session.setAttribute("ourScheme_obj", ourScheme);
        } // else

        ourScheme = new SourceAnalyzer();
        result_list = ourScheme.analyze(text);
        return ResponseEntity.ok(result_list.get(result_list.size() - 1).getResultTree().getJSONString());
        // return ResponseEntity.ok(result_list.get(result_list.size() - 1).toString());
    }
    
}

