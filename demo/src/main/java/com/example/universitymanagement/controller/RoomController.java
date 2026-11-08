package com.example.universitymanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.universitymanagement.model.Room;
import com.example.universitymanagement.service.RoomService;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomServ;

    RoomController(RoomService roomServ){
        this.roomServ = roomServ;
    }

    @GetMapping
    public String getAll(Model model){
        model.addAttribute("rooms", roomServ.getAll());
        return "room/index";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model){
        model.addAttribute("room", new Room());
        return "room/form";
    }

    @PostMapping
    public String save(@ModelAttribute Room room){
        roomServ.save(room);
        return "redirect:/rooms";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id){
        roomServ.delete(id);
        return "redirect:/rooms";
    }
}
