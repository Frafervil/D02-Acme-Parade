
package controllers.administrator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.HistoryService;
import services.MemberService;
import services.ParadeService;
import services.PositionService;
import services.RequestService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.Member;
import domain.Parade;

@Controller
@RequestMapping("/dashboard/administrator")
public class DashboardAdministratorController extends AbstractController {

	// Services

	@Autowired
	private MemberService		memberService;

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private ParadeService		paradeService;

	@Autowired
	private RequestService		requestService;

	@Autowired
	private PositionService		positionService;

	@Autowired
	private HistoryService		historyService;


	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		final ModelAndView result;
		final Double avgMemberPerBrotherhood, minMemberPerBrotherhood, maxMemberPerBrotherhood, stddevMemberPerBrotherhood;

		final Brotherhood largestBrotherhood;
		final Brotherhood smallestBroterhood;

		final Collection<Parade> startingSoonParade;

		final Double ratioPendingRequest, ratioapprovedRequest, ratioRejectedRequest;

		final Collection<Member> membersRequestapproved;

		final Map<String, Integer> positionStats;

		final Double avgRecordPerHistory, minRecordPerHistory, maxRecordPerHistory, stddevRecordPerHistory;

		final Brotherhood largestBrotherhoodHistory;

		final Collection<Brotherhood> brotherhoodsMoreThanAverage;
		// Stadistics

		// avg
		avgMemberPerBrotherhood = this.memberService.averageMemberPerBrotherhood();

		// min
		minMemberPerBrotherhood = this.memberService.minMemberPerBrotherhood();

		// max
		maxMemberPerBrotherhood = this.memberService.maxMemberPerBrotherhood();

		// standard Deviation
		stddevMemberPerBrotherhood = this.memberService.stddevMemberPerBrotherhood();

		// largest Brotherhood
		largestBrotherhood = this.brotherhoodService.largestBrotherhood();

		// smallest Brotherhood
		smallestBroterhood = this.brotherhoodService.smallestBrotherhood();

		// Starting Soon
		startingSoonParade = this.paradeService.startingSoonParades();

		// Ratios
		ratioPendingRequest = this.requestService.ratioPendingRequest();

		ratioapprovedRequest = this.requestService.ratioapprovedRequest();

		ratioRejectedRequest = this.requestService.ratioRejectedRequest();

		// Listing of members 10% approved

		membersRequestapproved = this.memberService.mostapprovedMembers();

		//Histogram

		positionStats = this.positionService.positionStats();
		final ArrayList<String> position = new ArrayList<>();
		final ArrayList<Integer> count = new ArrayList<>();

		for (final Entry<String, Integer> entry : positionStats.entrySet()) {
			position.add(entry.getKey());
			count.add(entry.getValue());
		}

		// History

		avgRecordPerHistory = this.historyService.avgRecordPerHistory();

		minRecordPerHistory = this.historyService.minRecordPerHistory();

		maxRecordPerHistory = this.historyService.maxRecordPerHistory();

		stddevRecordPerHistory = this.historyService.stddevRecordPerHistory();

		largestBrotherhoodHistory = this.historyService.largestBrotherhood();

		brotherhoodsMoreThanAverage = this.historyService.brotherhoodsMoreThanAverage();

		//
		result = new ModelAndView("administrator/dashboard");
		result.addObject("avgMemberPerBrotherhood", avgMemberPerBrotherhood);
		result.addObject("minMemberPerBrotherhood", minMemberPerBrotherhood);
		result.addObject("maxMemberPerBrotherhood", maxMemberPerBrotherhood);
		result.addObject("stddevMemberPerBrotherhood", stddevMemberPerBrotherhood);

		result.addObject("largestBrotherhood", largestBrotherhood);
		result.addObject("smallestBroterhood", smallestBroterhood);

		result.addObject("startingSoonParade", startingSoonParade);

		result.addObject("ratioPendingRequest", ratioPendingRequest);
		result.addObject("ratioapprovedRequest", ratioapprovedRequest);
		result.addObject("ratioRejectedRequest", ratioRejectedRequest);

		result.addObject("membersRequestapproved", membersRequestapproved);

		result.addObject("position", position);
		result.addObject("count", count);

		result.addObject("avgRecordPerHistory", avgRecordPerHistory);
		result.addObject("minRecordPerHistory", minRecordPerHistory);
		result.addObject("maxRecordPerHistory", maxRecordPerHistory);
		result.addObject("stddevRecordPerHistory", stddevRecordPerHistory);
		result.addObject("largestBrotherhoodHistory", largestBrotherhoodHistory);
		result.addObject("brotherhoodsMoreThanAverage", brotherhoodsMoreThanAverage);

		return result;

	}
}
