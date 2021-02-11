package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import javax.annotation.PostConstruct;

@Controller
public class DemoController {
	
	@Autowired
	UserRepository repos;
	
	/* 一覧画面（初期画面）への遷移 */
	@GetMapping
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();
		List<User> list = repos.findAll();
		mav.setViewName("users/list");
		mav.addObject("data", list);
		return mav;
	}
	
	/* 新規画面への遷移 */
	@GetMapping("/add")
	ModelAndView add() {
		ModelAndView mav = new ModelAndView();
		User data = new User();
		mav.addObject("formModel", data);
		mav.setViewName("users/new");
		return mav;
	}
	
	/* 新規作成処理 */
	@PostMapping("/create")
	@Transactional(readOnly=false)
	public ModelAndView create(
			@ModelAttribute("formModel") User user) {
		repos.saveAndFlush(user);
		return new ModelAndView("redirect:/");
	}
	
	/* 編集画面への遷移 */
	@GetMapping("/edit")
	ModelAndView edit(@RequestParam int id) {
		ModelAndView mav = new ModelAndView();
		User data = repos.findById(id);
		mav.addObject("formModel", data);
		mav.setViewName("users/new");
		return mav;
	}
	
	/* 更新処理 */
	@PostMapping()
	@Transactional(readOnly=false)
	public ModelAndView save(
			@ModelAttribute("formModel") User user) {
		repos.saveAndFlush(user);
		return new ModelAndView("redirect:users/list");
	}
	
	/* 削除処理 */
	@PostMapping("/delete")
	@Transactional(readOnly=false)
	public ModelAndView delete(@RequestParam int id) {
		repos.deleteById(id);
		return new ModelAndView("redirect:/");
	}
	
	/* 初期データ作成　*/
	@PostConstruct
	public void init() {
		User user1 = new User();
		user1.setName("島根　花子");
		user1.setAddress("島根県");
		user1.setTel("090-0000-1234");
		repos.saveAndFlush(user1);
		
		user1 = new User();
		user1.setName("大阪　太郎");
		user1.setAddress("大阪府");
		user1.setTel("090-1111-1234");
		repos.saveAndFlush(user1);
	}

}
