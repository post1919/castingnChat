package castingn.com.chat.web;


import castingn.com.chat.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
public class AdminController {

	@Autowired
	private ChatService chatService;

	@RequestMapping("/admin/memo")
	public ModelAndView memo(@RequestParam Map<String, Object> param) throws Exception {
		ModelAndView mv = new ModelAndView("jsonView");

		param.put("isGroup", true);
		List<Map<String, Object>> memoList = chatService.selectMemo(param);

		for (Map<String, Object> item : memoList) {
			param.replace("isGroup", false);
			param.put("inDate", item.get("MM_INDATE"));

			List<Map<String, Object>> memoSubList = chatService.selectMemo(param);
			item.put("list", memoSubList);
		}

		// 관리자 메모 내역
		mv.addObject("memo", memoList);
		return mv;
	}

	/**
	 * 메모 추가
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/admin/memo/add")
	public ModelAndView memoAdd(@RequestParam Map<String, Object> param) throws Exception {
		ModelAndView mv = new ModelAndView("jsonView");

		chatService.addMemo(param);
		mv.addObject("status", "1");
		return mv;
	}


	/**
	 * 메모 삭제
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/admin/memo/delete")
	public ModelAndView memoDelete(@RequestParam Map<String, Object> param) throws Exception {
		ModelAndView mv = new ModelAndView("jsonView");

		chatService.deleteMemo(param);
		mv.addObject("status", "1");
		return mv;
	}
}

