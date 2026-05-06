package com.projectit210.controller;

import com.projectit210.constant.AppConstant;
import com.projectit210.dto.request.EvaluationRequest;
import com.projectit210.entity.Lecturer;
import com.projectit210.entity.MentoringSession;
import com.projectit210.entity.User;
import com.projectit210.exception.BadRequestException;
import com.projectit210.exception.ResourceNotFoundException;
import com.projectit210.repository.LecturerRepository;
import com.projectit210.repository.MentoringSessionRepository;
import com.projectit210.service.AcademicEvaluationService;
import com.projectit210.service.EquipmentService;
import com.projectit210.service.MentoringSessionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/lecturer")
@RequiredArgsConstructor
public class LecturerController {

    private final MentoringSessionService sessionService;
    private final AcademicEvaluationService evaluationService;
    private final EquipmentService equipmentService;
    private final LecturerRepository lecturerRepository;
    private final MentoringSessionRepository sessionRepository;

    @GetMapping("/dashboard")
    public String dashboard(HttpServletRequest request, Model model) {
        User user = (User) request.getAttribute(AppConstant.CURRENT_USER);
        Lecturer lecturer = lecturerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Giảng viên không tồn tại"));
        model.addAttribute("user", user);
        model.addAttribute("lecturer", lecturer);
        model.addAttribute("pendingSessions", sessionService.getPendingSessionsByLecturer(lecturer.getId()));
        return "lecturer/dashboard";
    }

    @GetMapping("/pending-sessions")
    public String pendingSessions(HttpServletRequest request, Model model) {
        User user = (User) request.getAttribute(AppConstant.CURRENT_USER);
        Lecturer lecturer = lecturerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Giảng viên không tồn tại"));
        model.addAttribute("sessions", sessionService.getPendingSessionsByLecturer(lecturer.getId()));
        return "lecturer/pending-sessions";
    }

    @GetMapping("/evaluate/{sessionId}")
    public String evaluationForm(@PathVariable Long sessionId, Model model) {
        MentoringSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Buổi tư vấn không tồn tại"));
        model.addAttribute("session", session);
        model.addAttribute("evaluationRequest", new EvaluationRequest());
        model.addAttribute("equipments", equipmentService.findAllActive());
        return "lecturer/evaluation-form";
    }

    @PostMapping("/evaluate")
    public String submitEvaluation(@ModelAttribute EvaluationRequest evaluationRequest,
                                   HttpServletRequest request,
                                   RedirectAttributes redirectAttributes) {
        try {
            User user = (User) request.getAttribute(AppConstant.CURRENT_USER);
            Lecturer lecturer = lecturerRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Giảng viên không tồn tại"));
            evaluationService.completeEvaluation(lecturer.getId(), evaluationRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Đánh giá và tạo phiếu mượn thành công!");
        } catch (BadRequestException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/lecturer/pending-sessions";
    }
}
