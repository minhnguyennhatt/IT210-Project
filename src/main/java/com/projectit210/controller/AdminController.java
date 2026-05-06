package com.projectit210.controller;

import com.projectit210.constant.AppConstant;
import com.projectit210.dto.request.EquipmentRequest;
import com.projectit210.entity.User;
import com.projectit210.exception.BadRequestException;
import com.projectit210.service.BorrowingService;
import com.projectit210.service.EquipmentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final EquipmentService equipmentService;
    private final BorrowingService borrowingService;

    @GetMapping("/dashboard")
    public String dashboard(HttpServletRequest request, Model model) {
        User user = (User) request.getAttribute(AppConstant.CURRENT_USER);
        model.addAttribute("user", user);
        model.addAttribute("pendingBorrowings", borrowingService.getPendingDispatch());
        model.addAttribute("totalEquipments", equipmentService.findAll().size());
        return "admin/dashboard";
    }

    // ===================== EQUIPMENT CRUD (CORE-04) =====================

    @GetMapping("/equipments")
    public String equipmentList(Model model) {
        model.addAttribute("equipments", equipmentService.findAll());
        return "admin/equipments";
    }

    @GetMapping("/equipments/new")
    public String newEquipmentForm(Model model) {
        model.addAttribute("equipmentRequest", new EquipmentRequest());
        model.addAttribute("isEdit", false);
        return "admin/equipment-form";
    }

    @PostMapping("/equipments")
    public String createEquipment(@Valid @ModelAttribute EquipmentRequest equipmentRequest,
                                  BindingResult bindingResult,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", false);
            return "admin/equipment-form";
        }
        try {
            equipmentService.create(equipmentRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm thiết bị thành công!");
        } catch (BadRequestException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("isEdit", false);
            return "admin/equipment-form";
        }
        return "redirect:/admin/equipments";
    }

    @GetMapping("/equipments/{id}/edit")
    public String editEquipmentForm(@PathVariable Long id, Model model) {
        var eq = equipmentService.findById(id);
        EquipmentRequest request = EquipmentRequest.builder()
                .code(eq.getCode()).name(eq.getName())
                .description(eq.getDescription())
                .quantityInStock(eq.getQuantityInStock())
                .minimumStock(eq.getMinimumStock()).build();
        model.addAttribute("equipmentRequest", request);
        model.addAttribute("equipmentId", id);
        model.addAttribute("isEdit", true);
        return "admin/equipment-form";
    }

    @PostMapping("/equipments/{id}")
    public String updateEquipment(@PathVariable Long id,
                                  @Valid @ModelAttribute EquipmentRequest equipmentRequest,
                                  BindingResult bindingResult,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("equipmentId", id);
            model.addAttribute("isEdit", true);
            return "admin/equipment-form";
        }
        try {
            equipmentService.update(id, equipmentRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thiết bị thành công!");
        } catch (BadRequestException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("equipmentId", id);
            model.addAttribute("isEdit", true);
            return "admin/equipment-form";
        }
        return "redirect:/admin/equipments";
    }

    @PostMapping("/equipments/{id}/delete")
    public String deleteEquipment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        equipmentService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Xóa thiết bị thành công!");
        return "redirect:/admin/equipments";
    }

    // ===================== BORROWING MANAGEMENT (CORE-08) =====================

    @GetMapping("/borrowings")
    public String borrowingManagement(Model model) {
        model.addAttribute("borrowings", borrowingService.getAllBorrowings());
        model.addAttribute("pendingBorrowings", borrowingService.getPendingDispatch());
        return "admin/borrowing-management";
    }

    @PostMapping("/borrowings/{id}/approve")
    public String approveDispatch(@PathVariable Long id,
                                  HttpServletRequest request,
                                  RedirectAttributes redirectAttributes) {
        try {
            User user = (User) request.getAttribute(AppConstant.CURRENT_USER);
            borrowingService.approveDispatch(id, user.getId());
            redirectAttributes.addFlashAttribute("successMessage", "Xác nhận xuất kho thành công!");
        } catch (BadRequestException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/borrowings";
    }

    @PostMapping("/borrowings/{id}/reject")
    public String rejectBorrowing(@PathVariable Long id,
                                  HttpServletRequest request,
                                  RedirectAttributes redirectAttributes) {
        try {
            User user = (User) request.getAttribute(AppConstant.CURRENT_USER);
            borrowingService.rejectBorrowing(id, user.getId());
            redirectAttributes.addFlashAttribute("successMessage", "Đã từ chối phiếu mượn!");
        } catch (BadRequestException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/borrowings";
    }
}
