package springproject.member.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springproject.member.dto.MemberDTO;
import springproject.member.service.MemberService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    // 생성자 주입
    private final MemberService memberService;

    // 회원가입 페이지 출력 요청
    @GetMapping("/member/save")
    public String saveForm() {
        return "saveForm";
    }

//    @PostMapping("/member/save")
    public String saveV1(@RequestParam("memberEmail") String memberEmail,
                       @RequestParam("memberPassword") String memberPassword,
                       @RequestParam("memberName") String memberName) { //@RequestParam을 사용하여 saveFrom.html 페이지에서 보낸 데이터를 가져옴

        System.out.println("MemberController.save");
        System.out.println("memberEmail = " + memberEmail + ", memberPassword = " + memberPassword + ", memberName = " + memberName);
        return "index";
    }

    @PostMapping("/member/save")
    public String saveV2(@ModelAttribute MemberDTO memberDTO) { //DTO의 필드명과 받아온 name 속성이 같다면 자동으로 담음

        memberService.save(memberDTO);

        return "login";
    }

    @GetMapping("/member/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/member/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        MemberDTO loginResult = memberService.login(memberDTO);
        if (loginResult != null) {
            //login 성공
            // session 사용 로그인 유록
            session.setAttribute("loginEmail", loginResult.getMemberEmail());
            return "main";
        } else {
            //login 실패
            return "login-fail";
        }
    }

    @GetMapping("/member")
    public String findAll(Model model) {
        List<MemberDTO> memberDTOList = memberService.findAll();
        model.addAttribute("memberList", memberDTOList);
        return "list";
    }

    @GetMapping("/member/{memberId}")
    public String findById(@PathVariable Long memberId, Model model) {
        MemberDTO memberDTO = memberService.findById(memberId);
        if (memberDTO != null) {
            model.addAttribute("member", memberDTO);
            return "member";
        } else {
            return "member-fail";
        }
    }

    @GetMapping("/member/update")
    public String updateForm(HttpSession session, Model model) {
        String email = (String) session.getAttribute("loginEmail");
        MemberDTO memberDTO = memberService.updateForm(email);
        model.addAttribute("updateMember", memberDTO);
        return "update";
    }

    @PostMapping("/member/update")
    public String update(@ModelAttribute MemberDTO memberDTO) {
        memberService.update(memberDTO);
        return "redirect:/member/" + memberDTO.getMemberId();
    }

    @GetMapping("/member/delete/{memberId}")
    public String deleteById(@PathVariable Long memberId) {
        memberService.deleteById(memberId);
        return "redirect:/member";
    }

    @GetMapping("/member/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "index";
    }
}
