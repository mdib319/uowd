package org.uowd.sskrs.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.uowd.sskrs.models.ConstructionPractice;
import org.uowd.sskrs.models.ConstructionPracticeManager;
import org.uowd.sskrs.models.IdentificationRequest;
import org.uowd.sskrs.models.ImplementationRequest;
import org.uowd.sskrs.models.SecurityError;
import org.uowd.sskrs.models.SecurityErrorManager;
import org.uowd.sskrs.models.SecurityRequirement;
import org.uowd.sskrs.models.SecurityRequirementManager;
import org.uowd.sskrs.models.SoftwareFeature;
import org.uowd.sskrs.models.SoftwareParadigm;
import org.uowd.sskrs.models.SoftwareWeakness;
import org.uowd.sskrs.models.SoftwareWeaknessManager;
import org.uowd.sskrs.models.SubjectArea;
import org.uowd.sskrs.models.VerificationPractice;
import org.uowd.sskrs.models.VerificationPracticeManager;
import org.uowd.sskrs.models.VerificationRequest;

@Controller
public class MainController {

	private static final String STATUS = "status";
	private static final String MESSAGE = "message";

	private JdbcTemplate jdbcTemplate;

	@Autowired
	@Qualifier("dataSource")
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@GetMapping(path = "/s-scrum-utilities")
	public ModelAndView sScrumUtilitiesView() {
		return new ModelAndView("s-scrum-utilities");
	}

	@RequestMapping(value = "/s-scrum-utilities/security-identification", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView securityIdentificationView(HttpServletRequest httpRequest,
			@ModelAttribute("identificationRequest") IdentificationRequest identificationRequest) {
		
		ModelAndView mv = new ModelAndView("security-identification");
		
		List<Map<String,Object>> list = jdbcTemplate.queryForList("SELECT [ID], [DESCRIPTION] FROM [SSKMS].[dbo].[SOFTWARE_PARADIGM]");        
        List<SoftwareParadigm> spList = new ArrayList<>();        
        list.forEach(m -> {               
        	SoftwareParadigm sp = new SoftwareParadigm((int) m.get("ID"), (String) m.get("DESCRIPTION"));
        	spList.add(sp);               
        });
        
        mv.addObject("softwareParadigmList", spList);
        
        List<Map<String,Object>> list2 = jdbcTemplate.queryForList("SELECT [ID], [DESCRIPTION] FROM [SSKMS].[dbo].[SUBJECT_AREA]");        
        List<SubjectArea> spList2 = new ArrayList<>();        
        list2.forEach(m -> {               
        	SubjectArea sa = new SubjectArea((int) m.get("ID"), (String) m.get("DESCRIPTION"));
        	spList2.add(sa);
        });
        
        mv.addObject("subjectAreaList", spList2);

		if (httpRequest.getMethod().contentEquals("POST")) 
		{
			StringBuilder result = new StringBuilder();

			List<Map<String,Object>> list3 = jdbcTemplate.queryForList("SELECT SF.DESCRIPTION AS 'SOFTWARE FEATURE', SR.DESCRIPTION AS 'SECURITY REQUIREMENT', SE.DESCRIPTION AS 'SECURITY ERROR', SW.DESCRIPTION AS 'VULNERABILITY' "
					+ "FROM SOFTWARE_FEATURE AS SF "
					+ "INNER JOIN SOFTWARE_PARADIGM_HAS_SOFTWARE_FEATURE SPHSF ON SPHSF.SOFTWARE_FEATURE_ID = SF.ID "
					+ "INNER JOIN SUBJECT_AREA_HAS_SOFTWARE_FEATURE SAHSF ON SAHSF.SOFTWARE_FEATURE_ID = SF.ID "
					+ "INNER JOIN SOFTWARE_FEATURE_HAS_SECURITY_REQUIREMENT SFHSR ON SFHSR.SOFTWARE_FEATURE_ID = SF.ID "
					+ "INNER JOIN SECURITY_REQUIREMENT SR ON SR.ID = SFHSR.SECURITY_REQUIREMENT_ID "
					+ "INNER JOIN SECURITY_REQUIREMENT_ASSOCIATED_SECURITY_ERROR SRASE ON SRASE.SECURITY_REQUIREMENT_ID = SFHSR.SECURITY_REQUIREMENT_ID "
					+ "INNER JOIN SECURITY_ERROR SE ON SE.ID = SRASE.SECURITY_ERROR_ID "
					+ "INNER JOIN SECURITY_ERROR_CAUSES_SOFTWARE_WEAKNESS SECSW ON SECSW.SECURITY_ERROR_ID = SE.ID "
					+ "INNER JOIN SOFTWARE_WEAKNESS SW ON SW.ID = SECSW.SOFTWARE_WEAKNESS_ID "
					+ "WHERE SPHSF.SOFTWARE_PARADIGM_ID = ? AND SAHSF.SUBJECT_AREA_ID = ?", identificationRequest.getSoftwareParadigmId(), identificationRequest.getSubjectAreaId());
			
			list3.forEach(m -> {
				result.append("<tr>");
				result.append("<td>").append(m.get("SOFTWARE FEATURE")).append("</td>");
				result.append("<td>").append(m.get("SECURITY REQUIREMENT")).append("</td>");
				result.append("<td>").append(m.get("SECURITY ERROR")).append("</td>");
				result.append("<td>").append(m.get("VULNERABILITY")).append("</td>");
				result.append("</tr>");
	        });
		
			mv.addObject("result", result.toString());
		}

		return mv;
	}

	@RequestMapping(value = "/s-scrum-utilities/security-implementation", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView securityImplementationView(HttpServletRequest httpRequest,
			@ModelAttribute("implementationRequest") ImplementationRequest implementationRequest) {
		
		ModelAndView mv = new ModelAndView("security-implementation");

		List<Map<String,Object>> list = jdbcTemplate.queryForList("SELECT [ID], [DESCRIPTION] FROM [SSKMS].[dbo].[SOFTWARE_PARADIGM]");        
        List<SoftwareParadigm> spList = new ArrayList<>();        
        list.forEach(m -> {               
        	SoftwareParadigm sp = new SoftwareParadigm((int) m.get("ID"), (String) m.get("DESCRIPTION"));
        	spList.add(sp);               
        });
        
        mv.addObject("softwareParadigmList", spList);
        
        List<Map<String,Object>> list2 = jdbcTemplate.queryForList("SELECT [ID], [DESCRIPTION] FROM [SSKMS].[dbo].[SUBJECT_AREA]");        
        List<SubjectArea> spList2 = new ArrayList<>();        
        list2.forEach(m -> {               
        	SubjectArea sa = new SubjectArea((int) m.get("ID"), (String) m.get("DESCRIPTION"));
        	spList2.add(sa);
        });
        
        mv.addObject("subjectAreaList", spList2);

        if (httpRequest.getMethod().contentEquals("POST")) 
		{
			StringBuilder result = new StringBuilder();

			List<Map<String,Object>> list3 = jdbcTemplate.queryForList("SELECT SF.DESCRIPTION AS 'SOFTWARE FEATURE', SR.DESCRIPTION AS 'SECURITY REQUIREMENT', "
					+ "SE.DESCRIPTION AS 'SECURITY ERROR', SW.DESCRIPTION AS 'VULNERABILITY', " 
					+ "CP.DESCRIPTION AS 'CONSTRUCTION PRACTICE', CP.ID AS 'CONSTRUCTION ID' " 
					+ "FROM SOFTWARE_FEATURE AS SF "
					+ "INNER JOIN SOFTWARE_PARADIGM_HAS_SOFTWARE_FEATURE SPHSF ON SPHSF.SOFTWARE_FEATURE_ID = SF.ID "
					+ "INNER JOIN SUBJECT_AREA_HAS_SOFTWARE_FEATURE SAHSF ON SAHSF.SOFTWARE_FEATURE_ID = SF.ID "
					+ "INNER JOIN SOFTWARE_FEATURE_HAS_SECURITY_REQUIREMENT SFHSR ON SFHSR.SOFTWARE_FEATURE_ID = SF.ID "
					+ "INNER JOIN SECURITY_REQUIREMENT SR ON SR.ID = SFHSR.SECURITY_REQUIREMENT_ID "
					+ "INNER JOIN SECURITY_REQUIREMENT_ASSOCIATED_SECURITY_ERROR SRASE ON SRASE.SECURITY_REQUIREMENT_ID = SFHSR.SECURITY_REQUIREMENT_ID "
					+ "INNER JOIN SECURITY_ERROR SE ON SE.ID = SRASE.SECURITY_ERROR_ID "
					+ "INNER JOIN SECURITY_ERROR_CAUSES_SOFTWARE_WEAKNESS SECSW ON SECSW.SECURITY_ERROR_ID = SE.ID "
					+ "INNER JOIN SOFTWARE_WEAKNESS SW ON SW.ID = SECSW.SOFTWARE_WEAKNESS_ID "
					+ "INNER JOIN SOFTWARE_FEATURE_HAS_CONSTRUCTION_PRACTICE SFHCP ON SFHCP.SOFTWARE_FEATURE_ID = SF.ID "
					+ "INNER JOIN SECURITY_REQUIREMENT_FOLLOWED_BY_CONSTRUCTION_PRACTICE SRFBCP ON SRFBCP.SECURITY_REQUIREMENT_ID = SR.ID "
					+ "INNER JOIN SECURITY_ERROR_MITIGATED_BY_CONSTRUCTION_PRACTICE SEMBCP ON SEMBCP.SECURITY_ERROR_ID = SE.ID "
					+ "INNER JOIN CONSTRUCTION_PRACTICE CP ON CP.ID = SFHCP.CONSTRUCTION_PRACTICE_ID AND CP.ID = SRFBCP.CONSTRUCTION_PRACTICE_ID AND CP.ID = SEMBCP.CONSTRUCTION_PRACTICE_ID "
					+ "WHERE SPHSF.SOFTWARE_PARADIGM_ID = ? AND SAHSF.SUBJECT_AREA_ID = ?", implementationRequest.getSoftwareParadigmId(), implementationRequest.getSubjectAreaId());
			
			list3.forEach(m -> {
				result.append("<tr>");
				result.append("<td>").append(m.get("SOFTWARE FEATURE")).append("</td>");
				result.append("<td>").append(m.get("SECURITY REQUIREMENT")).append("</td>");
				result.append("<td>").append(m.get("SECURITY ERROR")).append("</td>");
				result.append("<td>").append(m.get("VULNERABILITY")).append("</td>");
				result.append("<td>").append(m.get("CONSTRUCTION PRACTICE")).append("</td>");
				result.append("<td><a target=\"_blank\" href=\"").append("/sskrs/view-practice?type=construction&id=").append(m.get("CONSTRUCTION ID")).append("\">View</a></td>");
				result.append("</tr>");
	        });
		
			mv.addObject("result", result.toString());
		}

		return mv;
	}

	@RequestMapping(value = "/s-scrum-utilities/security-verification", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView securityVerificationView(HttpServletRequest httpRequest,
			@ModelAttribute("verificationRequest") VerificationRequest verificationRequest) {
		
		ModelAndView mv = new ModelAndView("security-verification");

		List<Map<String,Object>> list = jdbcTemplate.queryForList("SELECT [ID], [DESCRIPTION] FROM [SSKMS].[dbo].[SOFTWARE_PARADIGM]");        
        List<SoftwareParadigm> spList = new ArrayList<>();        
        list.forEach(m -> {               
        	SoftwareParadigm sp = new SoftwareParadigm((int) m.get("ID"), (String) m.get("DESCRIPTION"));
        	spList.add(sp);               
        });
        
        mv.addObject("softwareParadigmList", spList);
        
        List<Map<String,Object>> list2 = jdbcTemplate.queryForList("SELECT [ID], [DESCRIPTION] FROM [SSKMS].[dbo].[SUBJECT_AREA]");        
        List<SubjectArea> spList2 = new ArrayList<>();        
        list2.forEach(m -> {               
        	SubjectArea sa = new SubjectArea((int) m.get("ID"), (String) m.get("DESCRIPTION"));
        	spList2.add(sa);
        });
        
        mv.addObject("subjectAreaList", spList2);

		if (httpRequest.getMethod().contentEquals("POST")) {
			StringBuilder result = new StringBuilder();

			List<Map<String,Object>> list3 = jdbcTemplate.queryForList("SELECT SF.DESCRIPTION AS 'SOFTWARE FEATURE', SR.DESCRIPTION AS 'SECURITY REQUIREMENT', "
					+ "SE.DESCRIPTION AS 'SECURITY ERROR', SW.DESCRIPTION AS 'VULNERABILITY', " 
					+ "VP.DESCRIPTION AS 'VERIFICATION PRACTICE', VP.ID AS 'VERIFICATION ID' " 
					+ "FROM SOFTWARE_FEATURE AS SF "
					+ "INNER JOIN SOFTWARE_PARADIGM_HAS_SOFTWARE_FEATURE SPHSF ON SPHSF.SOFTWARE_FEATURE_ID = SF.ID "
					+ "INNER JOIN SUBJECT_AREA_HAS_SOFTWARE_FEATURE SAHSF ON SAHSF.SOFTWARE_FEATURE_ID = SF.ID "
					+ "INNER JOIN SOFTWARE_FEATURE_HAS_SECURITY_REQUIREMENT SFHSR ON SFHSR.SOFTWARE_FEATURE_ID = SF.ID "
					+ "INNER JOIN SECURITY_REQUIREMENT SR ON SR.ID = SFHSR.SECURITY_REQUIREMENT_ID "
					+ "INNER JOIN SECURITY_REQUIREMENT_ASSOCIATED_SECURITY_ERROR SRASE ON SRASE.SECURITY_REQUIREMENT_ID = SFHSR.SECURITY_REQUIREMENT_ID "
					+ "INNER JOIN SECURITY_ERROR SE ON SE.ID = SRASE.SECURITY_ERROR_ID "
					+ "INNER JOIN SECURITY_ERROR_CAUSES_SOFTWARE_WEAKNESS SECSW ON SECSW.SECURITY_ERROR_ID = SE.ID "
					+ "INNER JOIN SOFTWARE_WEAKNESS SW ON SW.ID = SECSW.SOFTWARE_WEAKNESS_ID "
					+ "INNER JOIN SECURITY_REQUIREMENT_FOLLOWED_BY_VERIFICATION_PRACTICE SRFBVP ON SRFBVP.SECURITY_REQUIREMENT_ID = SR.ID "
					+ "INNER JOIN SECURITY_ERROR_SPOTTED_BY_VERIFICATION_PRACTICE SESBVP ON SESBVP.SECURITY_ERROR_ID = SE.ID "
					+ "INNER JOIN VERIFICATION_PRACTICE VP ON VP.ID = SRFBVP.VERIFICATION_PRACTICE_ID AND VP.ID = SESBVP.VERIFICATION_PRACTICE_ID "
					+ "WHERE SPHSF.SOFTWARE_PARADIGM_ID = ? AND SAHSF.SUBJECT_AREA_ID = ?", verificationRequest.getSoftwareParadigmId(), verificationRequest.getSubjectAreaId());
			
			list3.forEach(m -> {
				result.append("<tr>");
				result.append("<td>").append(m.get("SOFTWARE FEATURE")).append("</td>");
				result.append("<td>").append(m.get("SECURITY REQUIREMENT")).append("</td>");
				result.append("<td>").append(m.get("SECURITY ERROR")).append("</td>");
				result.append("<td>").append(m.get("VULNERABILITY")).append("</td>");
				result.append("<td>").append(m.get("VERIFICATION PRACTICE")).append("</td>");
				result.append("<td><a target=\"_blank\" href=\"").append("/sskrs/view-practice?type=verification&id=").append(m.get("VERIFICATION ID")).append("\">View</a></td>");
				result.append("</tr>");
	        });

			mv.addObject("result", result.toString());
		}

		return mv;
	}
	
	@GetMapping(path = "/view-practice")
	public ModelAndView viewPractice(@RequestParam(name = "type", required = true) String type, 
			@RequestParam(name = "id", required = true) String id) {
				
		ModelAndView mnv = new ModelAndView("view-practice");
		
		String tableName = "CONSTRUCTION_PRACTICE";
		
		if(type.equalsIgnoreCase("verification"))
			tableName = "VERIFICATION_PRACTICE";
		
		String practiceDescription = jdbcTemplate.queryForObject("SELECT DESCRIPTION FROM " + tableName + " WHERE ID = ?", String.class, id);		
		mnv.addObject("description", practiceDescription);
		
		if(type.equalsIgnoreCase("verification"))
		{
			List<Map<String,Object>> list = jdbcTemplate.queryForList("SELECT VP.DESCRIPTION 'VERIFICATION PRACTICE', SR.DESCRIPTION 'SECURITY REQUIREMENT', "
					+ "SF.DESCRIPTION 'SOFTWARE FEATURE', SE.DESCRIPTION 'SECURITY ERROR', "
					+ "SW.DESCRIPTION 'SOFTWARE WEAKNESS' "
					+ "FROM VERIFICATION_PRACTICE VP "
					+ "INNER JOIN SECURITY_REQUIREMENT_FOLLOWED_BY_VERIFICATION_PRACTICE SRFBVP ON SRFBVP.VERIFICATION_PRACTICE_ID = VP.ID "
					+ "INNER JOIN SECURITY_REQUIREMENT SR ON SR.ID = SRFBVP.SECURITY_REQUIREMENT_ID "
					+ "INNER JOIN SOFTWARE_FEATURE_HAS_SECURITY_REQUIREMENT SFHSR ON SFHSR.SECURITY_REQUIREMENT_ID = SR.ID "
					+ "INNER JOIN SOFTWARE_FEATURE SF ON SF.ID = SFHSR.SOFTWARE_FEATURE_ID "
					+ "INNER JOIN SECURITY_ERROR_SPOTTED_BY_VERIFICATION_PRACTICE SESBVP ON SESBVP.VERIFICATION_PRACTICE_ID = VP.ID "
					+ "INNER JOIN SECURITY_ERROR SE ON SE.ID = SESBVP.SECURITY_ERROR_ID "
					+ "INNER JOIN SECURITY_ERROR_CAUSES_SOFTWARE_WEAKNESS SECSW ON SECSW.SECURITY_ERROR_ID = SE.ID "
					+ "INNER JOIN SOFTWARE_WEAKNESS SW ON SW.ID = SECSW.SOFTWARE_WEAKNESS_ID "
					+ "WHERE VP.ID = ?", id);
			
	        List<HashMap<String, Object>> vpList = new ArrayList<>();
	        
	        list.forEach(m -> {               
	        	HashMap<String, Object> vpMap = new HashMap<>();
	        	vpMap.put("VERIFICATION PRACTICE", m.get("VERIFICATION PRACTICE"));
	        	vpMap.put("SECURITY REQUIREMENT", m.get("SECURITY REQUIREMENT"));
	        	vpMap.put("SOFTWARE FEATURE", m.get("SOFTWARE FEATURE"));
	        	vpMap.put("SECURITY ERROR", m.get("SECURITY ERROR"));
	        	vpMap.put("SOFTWARE WEAKNESS", m.get("SOFTWARE WEAKNESS"));
	        	
	        	vpList.add(vpMap);
	        });
	        
	        mnv.addObject("vpList", vpList);
	        
	        list = jdbcTemplate.queryForList("SELECT VP.DESCRIPTION 'VERIFICATION PRACTICE', A.DESCRIPTION 'APPROACH', "
	        		+ "T.DESCRIPTION 'TECHNIQUE', ST.DESCRIPTION 'SECURITY TOOL' "
	        		+ "FROM VERIFICATION_PRACTICE VP "
	        		+ "LEFT JOIN VERIFICATION_PRACTICE_HAS_APPROACH VPHA ON VPHA.VERIFICATION_PRACTICE_ID = VP.ID "
	        		+ "LEFT JOIN APPROACH A ON A.ID = VPHA.APPROACH_ID "
	        		+ "LEFT JOIN VERIFICATION_PRACTICE_HAS_TECHNIQUE VPHT ON VPHT.VERIFICATION_PRACTICE_ID = VP.ID "
	        		+ "LEFT JOIN TECHNIQUE T ON T.ID = VPHT.TECHNIQUE_ID "
	        		+ "LEFT JOIN TECHNIQUE_HAS_SECURITY_TOOL THST ON THST.TECHNIQUE_ID = T.ID "
	        		+ "LEFT JOIN SECURITY_TOOL ST ON ST.ID = THST.SECURITY_TOOL_ID "
	        		+ "WHERE VP.ID = ?", id);
			
	        List<HashMap<String, Object>> vList = new ArrayList<>();
	        
	        list.forEach(m -> {               
	        	HashMap<String, Object> vMap = new HashMap<>();
	        	vMap.put("VERIFICATION PRACTICE", m.get("VERIFICATION PRACTICE"));
	        	vMap.put("APPROACH", m.get("APPROACH"));
	        	vMap.put("TECHNIQUE", m.get("TECHNIQUE"));
	        	vMap.put("SECURITY TOOL", m.get("SECURITY TOOL"));
	        	
	        	vList.add(vMap);
	        });
	        
	        mnv.addObject("vList", vList);
		}
		else
		{
			List<Map<String,Object>> list = jdbcTemplate.queryForList("SELECT CP.DESCRIPTION 'CONSTRUCTION PRACTICE', SF.DESCRIPTION 'SOFTWARE FEATURE', "
					+ "SR.DESCRIPTION 'SECURITY REQUIREMENT', SE.DESCRIPTION 'SECURITY ERROR', SW.DESCRIPTION 'SOFTWARE WEAKNESS' "
					+ "FROM CONSTRUCTION_PRACTICE CP "
					+ "INNER JOIN SOFTWARE_FEATURE_HAS_CONSTRUCTION_PRACTICE SFHCP ON SFHCP.CONSTRUCTION_PRACTICE_ID = CP.ID "
					+ "INNER JOIN SOFTWARE_FEATURE SF ON SF.ID = SFHCP.SOFTWARE_FEATURE_ID "
					+ "INNER JOIN SECURITY_REQUIREMENT_FOLLOWED_BY_CONSTRUCTION_PRACTICE SRFCP ON SRFCP.CONSTRUCTION_PRACTICE_ID = CP.ID "
					+ "INNER JOIN SECURITY_REQUIREMENT SR ON SR.ID = SRFCP.SECURITY_REQUIREMENT_ID "
					+ "INNER JOIN SECURITY_ERROR_MITIGATED_BY_CONSTRUCTION_PRACTICE SEMBCP ON SEMBCP.CONSTRUCTION_PRACTICE_ID = CP.ID "
					+ "INNER JOIN SECURITY_ERROR SE ON SE.ID = SEMBCP.SECURITY_ERROR_ID "
					+ "INNER JOIN SECURITY_ERROR_CAUSES_SOFTWARE_WEAKNESS SECSW ON SECSW.SECURITY_ERROR_ID = SEMBCP.SECURITY_ERROR_ID "
					+ "INNER JOIN SOFTWARE_WEAKNESS SW ON SW.ID = SECSW.SOFTWARE_WEAKNESS_ID "
					+ "WHERE CP.ID = ?", id);
			
	        List<HashMap<String, Object>> cpList = new ArrayList<>();
	        
	        list.forEach(m -> {               
	        	HashMap<String, Object> cpMap = new HashMap<>();
	        	cpMap.put("CONSTRUCTION PRACTICE", m.get("CONSTRUCTION PRACTICE"));
	        	cpMap.put("SOFTWARE FEATURE", m.get("SOFTWARE FEATURE"));
	        	cpMap.put("SECURITY REQUIREMENT", m.get("SECURITY REQUIREMENT"));
	        	cpMap.put("SECURITY ERROR", m.get("SECURITY ERROR"));
	        	cpMap.put("SOFTWARE WEAKNESS", m.get("SOFTWARE WEAKNESS"));
	        	
	        	cpList.add(cpMap);
	        });
	        
	        mnv.addObject("cpList", cpList);
	        
	        list = jdbcTemplate.queryForList("SELECT CP.DESCRIPTION 'CONSTRUCTION PRACTICE', S.DESCRIPTION 'STRATEGY', " 
	        		+ "M.DESCRIPTION 'METHOD', L.DESCRIPTION 'LANGUAGE', "
	        		+ "MC.DESCRIPTION 'MECHANISM', ST.DESCRIPTION 'SECURITY TOOL' "
	        		+ "FROM CONSTRUCTION_PRACTICE CP "
	        		+ "LEFT JOIN CONSTRUCTION_PRACTICE_FOLLOWS_STRATEGY CPFS ON CPFS.CONSTRUCTION_PRACTICE_ID = CP.ID "
	        		+ "LEFT JOIN STRATEGY S ON S.ID = CPFS.STRATEGY_ID "
	        		+ "LEFT JOIN CONSTRUCTION_PRACTICE_HAS_METHOD CPHM ON CPHM.CONSTRUCTION_PRACTICE_ID = CP.ID "
	        		+ "LEFT JOIN METHOD M ON M.ID = CPHM.METHOD_ID "
	        		+ "LEFT JOIN METHOD_RELATED_LANGUAGE MRL ON MRL.METHOD_ID = M.ID "
	        		+ "LEFT JOIN LANGUAGE L ON L.ID = MRL.LANGUAGE_ID "
	        		+ "LEFT JOIN METHOD_HAS_MECHANISM MHM ON MHM.METHOD_ID = M.ID "
	        		+ "LEFT JOIN MECHANISM MC ON MC.ID = MHM.MECHANISM_ID "
	        		+ "LEFT JOIN MECHANISM_UTILIZES_SECURITY_TOOL MUST ON MUST.MECHANISM_ID = MC.ID "
	        		+ "LEFT JOIN SECURITY_TOOL ST ON ST.ID = MUST.SECURITY_TOOL_ID "
	        		+ "WHERE CP.ID = ?", id);
			
	        List<HashMap<String, Object>> pList = new ArrayList<>();
	        
	        list.forEach(m -> {               
	        	HashMap<String, Object> pMap = new HashMap<>();
	        	pMap.put("CONSTRUCTION PRACTICE", m.get("CONSTRUCTION PRACTICE"));
	        	pMap.put("STRATEGY", m.get("STRATEGY"));
	        	pMap.put("METHOD", m.get("METHOD"));
	        	pMap.put("LANGUAGE", m.get("LANGUAGE"));
	        	pMap.put("MECHANISM", m.get("MECHANISM"));
	        	pMap.put("SECURITY TOOL", m.get("SECURITY TOOL"));
	        	
	        	pList.add(pMap);
	        });
	        
	        mnv.addObject("pList", pList);
		}
		
		return mnv;
	}

	@GetMapping(path = "/security-acquisition")
	public ModelAndView securityAcquisitionView() {
		return new ModelAndView("security-acquisition");
	}
	
	@RequestMapping(path = "/security-acquisition/msp", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView manageSoftwareParadigmView(@ModelAttribute("softwareParadigm") SoftwareParadigm softwareParadigm, 
			@RequestParam(name = "action", defaultValue = "init", required = false) String action) {

		ModelAndView mnv = new ModelAndView("msp");
        
        if(action.contentEquals("add"))
        {
        	if (softwareParadigm.getDescription().trim().isEmpty()) 
    		{
    			mnv.addObject(STATUS, "0");
    			mnv.addObject(MESSAGE, "Software Paradigm Name may not be empty.");
    		}
    		else
    		{
    			int numOfRecords = jdbcTemplate.queryForObject(
    					"SELECT COUNT(*) AS NUMOFRECORDS FROM [SSKMS].[dbo].[SOFTWARE_PARADIGM] WHERE CAST(DESCRIPTION AS varchar(MAX)) = ?",
    					(rs, rowNum) -> rs.getInt("NUMOFRECORDS"), softwareParadigm.getDescription());

    			if (numOfRecords > 0) 
    			{
    				mnv.addObject(STATUS, "0");
    				mnv.addObject(MESSAGE, "Software Paradigm '" + softwareParadigm.getDescription() + "' already exists.");
    			}
    			else
    			{
    				int rows = jdbcTemplate.update("INSERT INTO [dbo].[SOFTWARE_PARADIGM] ([DESCRIPTION]) VALUES (?)", softwareParadigm.getDescription());

    				if (rows == 1)
    				{
    					mnv.addObject(STATUS, "1");
    					mnv.addObject(MESSAGE,
    							"Software Paradigm '" + softwareParadigm.getDescription() + "' added successfully.");
    				}
    				else
    				{
    					mnv.addObject(STATUS, "0");
    					mnv.addObject(MESSAGE,
    							"Error occurred while adding Software Paradigm '" + softwareParadigm.getDescription() + "'.");
    				}
    			}
    		}
        }
        else if(action.contentEquals("delete"))
        {
        	if(softwareParadigm.getId() == -1)
        	{
        		mnv.addObject(STATUS, "0");
    			mnv.addObject(MESSAGE, "Software Paradigm Id is not valid.");
        	}
        	else
        	{
        		int numOfRecords = jdbcTemplate.queryForObject(
    					"SELECT COUNT(*) AS 'NUM OF SOFTWARE FEATURES' FROM SOFTWARE_FEATURE AS SF INNER JOIN SOFTWARE_PARADIGM_HAS_SOFTWARE_FEATURE AS SPHSF ON SPHSF.SOFTWARE_FEATURE_ID = SF.ID WHERE SPHSF.SOFTWARE_PARADIGM_ID = ?",
    					(rs, rowNum) -> rs.getInt("NUM OF SOFTWARE FEATURES"), softwareParadigm.getId());

    			if (numOfRecords > 0) 
    			{
    				mnv.addObject(STATUS, "0");
    				mnv.addObject(MESSAGE, "Software Paradigm '" + softwareParadigm.getDescription() + "' has " + numOfRecords + " Software Feature(s) linked to it.");
    			}
    			else
    			{
	        		int rows = jdbcTemplate.update("DELETE FROM [dbo].[SOFTWARE_PARADIGM] WHERE ID = ?", softwareParadigm.getId());
	
					if (rows == 1)
					{
						mnv.addObject(STATUS, "1");
						mnv.addObject(MESSAGE,
								"Software Paradigm '" + softwareParadigm.getDescription() + "' deleted successfully.");
					}
					else
					{
						mnv.addObject(STATUS, "0");
						mnv.addObject(MESSAGE,
								"Error occurred while deleting Software Paradigm '" + softwareParadigm.getDescription() + "'.");
					}
    			}
        	}
        }
        
        mnv.addObject("softwareParadigm", new SoftwareParadigm());
        
        List<Map<String,Object>> list = jdbcTemplate.queryForList("SELECT [ID], [DESCRIPTION] FROM [SSKMS].[dbo].[SOFTWARE_PARADIGM]");        
        List<SoftwareParadigm> spList = new ArrayList<>();        
        list.forEach(m -> {               
        	SoftwareParadigm sp = new SoftwareParadigm((int) m.get("ID"), (String) m.get("DESCRIPTION"));
        	spList.add(sp);               
        });
        
        mnv.addObject("softwareParadigmList", spList);

		return mnv;
	}

	@RequestMapping(path = "/security-acquisition/msa", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView manageSubjectAreaView(@ModelAttribute("subjectArea") SubjectArea subjectArea, 
			@RequestParam(name = "action", defaultValue = "init", required = false) String action) {
		
		ModelAndView mnv = new ModelAndView("msa");
		
		if(action.contentEquals("add"))
        {
        	if (subjectArea.getDescription().trim().isEmpty()) 
    		{
    			mnv.addObject(STATUS, "0");
    			mnv.addObject(MESSAGE, "Subject Area Name may not be empty.");
    		}
    		else
    		{
    			int numOfRecords = jdbcTemplate.queryForObject(
    					"SELECT COUNT(*) AS NUMOFRECORDS FROM [SSKMS].[dbo].[SUBJECT_AREA] WHERE CAST(DESCRIPTION AS varchar(MAX)) = ?",
    					(rs, rowNum) -> rs.getInt("NUMOFRECORDS"), subjectArea.getDescription());

    			if (numOfRecords > 0) 
    			{
    				mnv.addObject(STATUS, "0");
    				mnv.addObject(MESSAGE, "Subject Area '" + subjectArea.getDescription() + "' already exists.");
    			}
    			else
    			{
    				int rows = jdbcTemplate.update("INSERT INTO [dbo].[SUBJECT_AREA] ([DESCRIPTION]) VALUES (?)", subjectArea.getDescription());

    				if (rows == 1)
    				{
    					mnv.addObject(STATUS, "1");
    					mnv.addObject(MESSAGE,
    							"Subject Area '" + subjectArea.getDescription() + "' added successfully.");
    				}
    				else
    				{
    					mnv.addObject(STATUS, "0");
    					mnv.addObject(MESSAGE,
    							"Error occurred while adding Subject Area '" + subjectArea.getDescription() + "'.");
    				}
    			}
    		}
        }
        else if(action.contentEquals("delete"))
        {
        	if(subjectArea.getId() == -1)
        	{
        		mnv.addObject(STATUS, "0");
    			mnv.addObject(MESSAGE, "Subject Area Id is not valid.");
        	}
        	else
        	{
        		int numOfRecords = jdbcTemplate.queryForObject(
    					"SELECT COUNT(*) AS 'NUM OF SOFTWARE FEATURES' FROM SOFTWARE_FEATURE AS SF INNER JOIN SUBJECT_AREA_HAS_SOFTWARE_FEATURE AS SAHSF ON SAHSF.SOFTWARE_FEATURE_ID = SF.ID WHERE SAHSF.SUBJECT_AREA_ID = ?",
    					(rs, rowNum) -> rs.getInt("NUM OF SOFTWARE FEATURES"), subjectArea.getId());

    			if (numOfRecords > 0) 
    			{
    				mnv.addObject(STATUS, "0");
    				mnv.addObject(MESSAGE, "Subject Area '" + subjectArea.getDescription() + "' has " + numOfRecords + " Software Feature(s) linked to it.");
    			}
    			else
    			{
	        		int rows = jdbcTemplate.update("DELETE FROM [dbo].[SUBJECT_AREA] WHERE ID = ?", subjectArea.getId());
	
					if (rows == 1)
					{
						mnv.addObject(STATUS, "1");
						mnv.addObject(MESSAGE,
								"Subject Area '" + subjectArea.getDescription() + "' deleted successfully.");
					}
					else
					{
						mnv.addObject(STATUS, "0");
						mnv.addObject(MESSAGE,
								"Error occurred while deleting Subject Area '" + subjectArea.getDescription() + "'.");
					}
    			}
        	}
        }
        
        mnv.addObject("subjectArea", new SubjectArea());
        
        List<Map<String,Object>> list = jdbcTemplate.queryForList("SELECT [ID], [DESCRIPTION] FROM [SSKMS].[dbo].[SUBJECT_AREA]");        
        List<SubjectArea> saList = new ArrayList<>();        
        list.forEach(m -> {               
        	SubjectArea sa = new SubjectArea((int) m.get("ID"), (String) m.get("DESCRIPTION"));
        	saList.add(sa);
        });
        
        mnv.addObject("subjectAreaList", saList);
		
		return mnv;
	}

	@RequestMapping(path = "/security-acquisition/msf", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView manageSoftwareFeatureView(@ModelAttribute("softwareFeature") SoftwareFeature softwareFeature, 
			@RequestParam(name = "action", defaultValue = "init", required = false) String action) {
		ModelAndView mnv = new ModelAndView("msf");
		
		if(action.contentEquals("add"))
        {
        	if (softwareFeature.getDescription().trim().isEmpty()) 
    		{
    			mnv.addObject(STATUS, "0");
    			mnv.addObject(MESSAGE, "Software Feature Name may not be empty.");
    		}
    		else
    		{
    			if(softwareFeature.getSoftwareParadigmId().trim().isEmpty() || softwareFeature.getSoftwareParadigmId().contentEquals("-1"))
    			{
    				mnv.addObject(STATUS, "0");
        			mnv.addObject(MESSAGE, "Software Paradigm should be selected.");
    			}
    			else
    			{
    				if(softwareFeature.getSubjectAreaId().trim().isEmpty() || softwareFeature.getSubjectAreaId().contentEquals("-1"))
    				{
    					mnv.addObject(STATUS, "0");
            			mnv.addObject(MESSAGE, "Subject Area should be selected.");
    				}
    				else
    				{
		    			int numOfRecords = jdbcTemplate.queryForObject(
		    					"SELECT COUNT(*) AS NUMOFRECORDS FROM [SSKMS].[dbo].[SOFTWARE_FEATURE] WHERE CAST(DESCRIPTION AS varchar(MAX)) = ?",
		    					(rs, rowNum) -> rs.getInt("NUMOFRECORDS"), softwareFeature.getDescription());
		
		    			if (numOfRecords > 0) 
		    			{
		    				mnv.addObject(STATUS, "0");
		    				mnv.addObject(MESSAGE, "Software Feature '" + softwareFeature.getDescription() + "' already exists.");
		    			}
		    			else
		    			{
		    				int rows = jdbcTemplate.update("INSERT INTO [dbo].[SOFTWARE_FEATURE] ([DESCRIPTION]) VALUES (?)", softwareFeature.getDescription());
		
		    				if (rows == 1)
		    				{
		    					try
		    					{
			    					int softwareFeatureId = jdbcTemplate.queryForObject("SELECT ID FROM [dbo].[SOFTWARE_FEATURE] WHERE CAST(DESCRIPTION AS varchar(MAX)) = ?", Integer.class, softwareFeature.getDescription());
			    					
			    					rows = jdbcTemplate.update("INSERT INTO [dbo].[SOFTWARE_PARADIGM_HAS_SOFTWARE_FEATURE] ([SOFTWARE_PARADIGM_ID] ,[SOFTWARE_FEATURE_ID]) VALUES (?, ?)", 
			    							Integer.parseInt(softwareFeature.getSoftwareParadigmId()), softwareFeatureId);
			    					
			    					if(rows == 1)
			    					{
			    						rows = jdbcTemplate.update("INSERT INTO [dbo].[SUBJECT_AREA_HAS_SOFTWARE_FEATURE] ([SUBJECT_AREA_ID] ,[SOFTWARE_FEATURE_ID]) VALUES (?, ?)", 
				    							Integer.parseInt(softwareFeature.getSubjectAreaId()), softwareFeatureId);
				    					
			    						if(rows == 1)
			    						{
					    					mnv.addObject(STATUS, "1");
					    					mnv.addObject(MESSAGE,
					    							"Software Feature '" + softwareFeature.getDescription() + "' added successfully.");
			    						}
			    						else
			    						{
			    							mnv.addObject(STATUS, "0");
					    					mnv.addObject(MESSAGE,
					    							"Error occurred (4) while adding Software Feature '" + softwareFeature.getDescription() + "'.");
			    						}
			    					}
			    					else
			    					{
			    						mnv.addObject(STATUS, "0");
				    					mnv.addObject(MESSAGE,
				    							"Error occurred (3) while adding Software Feature '" + softwareFeature.getDescription() + "'.");
			    					}
		    					}
		    					catch(Exception ex)
		    					{
		    						mnv.addObject(STATUS, "0");
			    					mnv.addObject(MESSAGE,
			    							"Error occurred (2) while adding Software Feature '" + softwareFeature.getDescription() + "'.");
		    					}
		    				}
		    				else
		    				{
		    					mnv.addObject(STATUS, "0");
		    					mnv.addObject(MESSAGE,
		    							"Error occurred (1) while adding Software Feature '" + softwareFeature.getDescription() + "'.");
		    				}
		    			}
    				}
    			}
    		}
        }
        else if(action.contentEquals("delete"))
        {
        	if(softwareFeature.getId() == -1)
        	{
        		mnv.addObject(STATUS, "0");
    			mnv.addObject(MESSAGE, "Software Feature Id is not valid.");
        	}
        	else
        	{
        		int numOfRecords = jdbcTemplate.queryForObject(
        				"SELECT COUNT(*) AS 'NUM OF SOFTWARE FEATURES' " 
        				+ "FROM SOFTWARE_FEATURE_HAS_SECURITY_REQUIREMENT AS SFHSR "
        				+ "WHERE SFHSR.SOFTWARE_FEATURE_ID = ?",
    					(rs, rowNum) -> rs.getInt("NUM OF SOFTWARE FEATURES"), softwareFeature.getId());

    			if (numOfRecords > 0) 
    			{
    				mnv.addObject(STATUS, "0");
    				mnv.addObject(MESSAGE, "Software Feature '" + softwareFeature.getDescription() + "' has " + numOfRecords + " Security Requirement(s) linked to it.");
    			}
    			else
    			{
    				numOfRecords = jdbcTemplate.queryForObject(
            				"SELECT COUNT(*) AS 'NUM OF SOFTWARE FEATURES' " 
            				+ "FROM SOFTWARE_FEATURE_HAS_CONSTRUCTION_PRACTICE AS SFHCP "
            				+ "WHERE SFHCP.SOFTWARE_FEATURE_ID = ?",
        					(rs, rowNum) -> rs.getInt("NUM OF SOFTWARE FEATURES"), softwareFeature.getId());

        			if (numOfRecords > 0) 
        			{
        				mnv.addObject(STATUS, "0");
        				mnv.addObject(MESSAGE, "Software Feature '" + softwareFeature.getDescription() + "' has " + numOfRecords + " Construction Practice(s) linked to it.");
        			}
    				else
    				{
    					jdbcTemplate.update("DELETE FROM SOFTWARE_PARADIGM_HAS_SOFTWARE_FEATURE WHERE SOFTWARE_FEATURE_ID = ?", softwareFeature.getId());
    					jdbcTemplate.update("DELETE FROM SUBJECT_AREA_HAS_SOFTWARE_FEATURE WHERE SOFTWARE_FEATURE_ID = ?", softwareFeature.getId());    					
		        		
    					
    					int rows = jdbcTemplate.update("DELETE FROM [dbo].[SOFTWARE_FEATURE] WHERE ID = ?", softwareFeature.getId());
		
						if (rows == 1)
						{
							mnv.addObject(STATUS, "1");
							mnv.addObject(MESSAGE,
									"Software Feature '" + softwareFeature.getDescription() + "' deleted successfully.");
						}
						else
						{
							mnv.addObject(STATUS, "0");
							mnv.addObject(MESSAGE,
									"Error occurred while deleting Software Feature '" + softwareFeature.getDescription() + "'.");
						}
    				}
    			}
        	}
        }
        else if(action.contentEquals("edit"))
        {
        	if (softwareFeature.getDescription().trim().isEmpty()) 
    		{
    			mnv.addObject(STATUS, "0");
    			mnv.addObject(MESSAGE, "Software Feature Name may not be empty.");
    		}
    		else
    		{
    			if(softwareFeature.getSoftwareParadigmId().trim().isEmpty() || softwareFeature.getSoftwareParadigmId().contentEquals("-1"))
    			{
    				mnv.addObject(STATUS, "0");
        			mnv.addObject(MESSAGE, "Software Paradigm should be selected.");
    			}
    			else
    			{
    				if(softwareFeature.getSubjectAreaId().trim().isEmpty() || softwareFeature.getSubjectAreaId().contentEquals("-1"))
    				{
    					mnv.addObject(STATUS, "0");
            			mnv.addObject(MESSAGE, "Subject Area should be selected.");
    				}
    				else
    				{
		    			int numOfRecords = jdbcTemplate.queryForObject(
		    					"SELECT COUNT(*) AS NUMOFRECORDS FROM [SSKMS].[dbo].[SOFTWARE_FEATURE] WHERE CAST(DESCRIPTION AS varchar(MAX)) = ? AND ID <> ?",
		    					(rs, rowNum) -> rs.getInt("NUMOFRECORDS"), softwareFeature.getDescription(), softwareFeature.getId());
		
		    			if (numOfRecords > 0) 
		    			{
		    				mnv.addObject(STATUS, "0");
		    				mnv.addObject(MESSAGE, "Software Feature '" + softwareFeature.getDescription() + "' is duplicate.");
		    			}
		    			else
		    			{
		    				int rows = jdbcTemplate.update("UPDATE [dbo].[SOFTWARE_FEATURE] SET [DESCRIPTION] = ? WHERE ID = ?", softwareFeature.getDescription(), softwareFeature.getId());
		
		    				if (rows == 1)
		    				{
		    					try
		    					{
		    						jdbcTemplate.update("DELETE FROM [dbo].[SOFTWARE_PARADIGM_HAS_SOFTWARE_FEATURE] WHERE SOFTWARE_FEATURE_ID = ?", softwareFeature.getId());
		    						
		    						jdbcTemplate.update("DELETE FROM [dbo].[SUBJECT_AREA_HAS_SOFTWARE_FEATURE] WHERE SOFTWARE_FEATURE_ID = ?", softwareFeature.getId());
		    						
			    					rows = jdbcTemplate.update("INSERT INTO [dbo].[SOFTWARE_PARADIGM_HAS_SOFTWARE_FEATURE] ([SOFTWARE_PARADIGM_ID] ,[SOFTWARE_FEATURE_ID]) VALUES (?, ?)", 
			    							Integer.parseInt(softwareFeature.getSoftwareParadigmId()), softwareFeature.getId());
			    					
			    					if(rows == 1)
			    					{
			    						rows = jdbcTemplate.update("INSERT INTO [dbo].[SUBJECT_AREA_HAS_SOFTWARE_FEATURE] ([SUBJECT_AREA_ID], [SOFTWARE_FEATURE_ID]) VALUES (?, ?)", 
				    							Integer.parseInt(softwareFeature.getSubjectAreaId()), softwareFeature.getId());
				    					
			    						if(rows == 1)
			    						{
			    							mnv.addObject(STATUS, "1");
			    							mnv.addObject(MESSAGE,
					    							"Software Feature '" + softwareFeature.getDescription() + "' edited successfully.");
			    						}
			    						else
			    						{
			    							mnv.addObject(STATUS, "0");
					    					mnv.addObject(MESSAGE,
					    							"Error occurred (4) while editing Software Feature '" + softwareFeature.getDescription() + "'.");
			    						}
			    					}
			    					else
			    					{
			    						mnv.addObject(STATUS, "0");
				    					mnv.addObject(MESSAGE,
				    							"Error occurred (3) while editing Software Feature '" + softwareFeature.getDescription() + "'.");
			    					}
		    					}
		    					catch(Exception ex)
		    					{
		    						ex.printStackTrace();
		    						
		    						mnv.addObject(STATUS, "0");
			    					mnv.addObject(MESSAGE,
			    							"Error occurred (2) while editing Software Feature '" + softwareFeature.getDescription() + "'.");
		    					}
		    				}
		    				else
		    				{
		    					mnv.addObject(STATUS, "0");
		    					mnv.addObject(MESSAGE,
		    							"Error occurred (1) while editing Software Feature '" + softwareFeature.getDescription() + "'.");
		    				}
		    			}
    				}
    			}
    		}
        }
        
        if(!action.contentEquals("initEdit") && !action.contentEquals("edit"))
        {
        	mnv.addObject("softwareFeature", new SoftwareFeature());
        }
        
        List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT SF.ID AS 'SOFTWARE FEATURE ID', SF.DESCRIPTION AS 'SOFTWARE FEATURE DESCRIPTION', "
        		+ "SP.ID AS 'SOFTWARE PARADIGM ID', SP.DESCRIPTION AS 'SOFTWARE PARADIGM DESCRIPTION', "
        		+ "SA.ID AS 'SUBJECT AREA ID', SA.DESCRIPTION AS 'SUBJECT AREA DESCRIPTION' "
        		+ "FROM [SSKMS].[dbo].[SOFTWARE_FEATURE] AS SF "
        		+ "INNER JOIN SOFTWARE_PARADIGM_HAS_SOFTWARE_FEATURE AS SPHSF ON SPHSF.SOFTWARE_FEATURE_ID = SF.ID "
        		+ "INNER JOIN SOFTWARE_PARADIGM AS SP ON SP.ID = SPHSF.SOFTWARE_PARADIGM_ID "
        		+ "INNER JOIN SUBJECT_AREA_HAS_SOFTWARE_FEATURE AS SAHSF ON SAHSF.SOFTWARE_FEATURE_ID = SF.ID "
        		+ "INNER JOIN SUBJECT_AREA AS SA ON SA.ID = SAHSF.SUBJECT_AREA_ID "
        		+ "ORDER BY CAST(SF.DESCRIPTION AS VARCHAR(MAX)), CAST(SP.DESCRIPTION AS VARCHAR(MAX)), CAST(SA.DESCRIPTION AS VARCHAR(MAX)) ASC");
        
        List<SoftwareFeature> sfList = new ArrayList<>();        
        list.forEach(m -> {               
        	SoftwareFeature sf = new SoftwareFeature((int) m.get("SOFTWARE FEATURE ID"), (String) m.get("SOFTWARE FEATURE DESCRIPTION"),
        			"" + (int) m.get("SOFTWARE PARADIGM ID"), (String) m.get("SOFTWARE PARADIGM DESCRIPTION"),
        			"" + (int) m.get("SUBJECT AREA ID"), (String) m.get("SUBJECT AREA DESCRIPTION"));
        	
        	sfList.add(sf);
        });
        
        mnv.addObject("softwareFeatureList", sfList);
        
        list = jdbcTemplate.queryForList("SELECT * FROM SOFTWARE_PARADIGM");
        
        List<SoftwareParadigm> spList = new ArrayList<>();
        list.forEach(m -> {
        	SoftwareParadigm sp = new SoftwareParadigm((int) m.get("ID"), (String) m.get("DESCRIPTION"));
        	
        	spList.add(sp);
        });
		
        mnv.addObject("softwareParadigmList", spList);
        
        list = jdbcTemplate.queryForList("SELECT * FROM SUBJECT_AREA");
        
        List<SubjectArea> saList = new ArrayList<>();
        list.forEach(m -> {
        	SubjectArea sa = new SubjectArea((int) m.get("ID"), (String) m.get("DESCRIPTION"));
        	
        	saList.add(sa);
        });
		
        mnv.addObject("subjectAreaList", saList);
        
		return mnv;
	}
	
	@RequestMapping(path = "/security-acquisition/msr", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView manageSecurityRequirementView(@ModelAttribute("securityRequirementManager") SecurityRequirementManager securityRequirementManager, HttpServletRequest request) {
		ModelAndView mnv = new ModelAndView("msr");
		
		if (request.getMethod().equals(RequestMethod.POST.name())) {
			
			List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT SF.ID AS 'SOFTWARE FEATURE ID', SF.DESCRIPTION AS 'SOFTWARE FEATURE DESCRIPTION', "
	        		+ "SP.ID AS 'SOFTWARE PARADIGM ID', SP.DESCRIPTION AS 'SOFTWARE PARADIGM DESCRIPTION', "
	        		+ "SA.ID AS 'SUBJECT AREA ID', SA.DESCRIPTION AS 'SUBJECT AREA DESCRIPTION' "
	        		+ "FROM [SSKMS].[dbo].[SOFTWARE_FEATURE] AS SF "
	        		+ "INNER JOIN SOFTWARE_PARADIGM_HAS_SOFTWARE_FEATURE AS SPHSF ON SPHSF.SOFTWARE_FEATURE_ID = SF.ID "
	        		+ "INNER JOIN SOFTWARE_PARADIGM AS SP ON SP.ID = SPHSF.SOFTWARE_PARADIGM_ID "
	        		+ "INNER JOIN SUBJECT_AREA_HAS_SOFTWARE_FEATURE AS SAHSF ON SAHSF.SOFTWARE_FEATURE_ID = SF.ID "
	        		+ "INNER JOIN SUBJECT_AREA AS SA ON SA.ID = SAHSF.SUBJECT_AREA_ID "
	        		+ "WHERE SP.ID = ? AND SA.ID = ? "
	        		+ "ORDER BY CAST(SF.DESCRIPTION AS VARCHAR(MAX)), CAST(SP.DESCRIPTION AS VARCHAR(MAX)), CAST(SA.DESCRIPTION AS VARCHAR(MAX)) ASC"
	        		, securityRequirementManager.getSoftwareParadigmId(), securityRequirementManager.getSubjectAreaId());
	        
	        List<SecurityRequirementManager> srList = new ArrayList<>();        
	        list.forEach(m -> {               
	        	SecurityRequirementManager sr = new SecurityRequirementManager("" + (int) m.get("SOFTWARE FEATURE ID"), (String) m.get("SOFTWARE FEATURE DESCRIPTION"),
	        			"" + (int) m.get("SOFTWARE PARADIGM ID"), (String) m.get("SOFTWARE PARADIGM DESCRIPTION"),
	        			"" + (int) m.get("SUBJECT AREA ID"), (String) m.get("SUBJECT AREA DESCRIPTION"),
	        			"-1", "", "");
	        	
	        	srList.add(sr);
	        });
			
			mnv.addObject("result", srList);
		}
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT * FROM SOFTWARE_PARADIGM");
        
        List<SoftwareParadigm> spList = new ArrayList<>();
        list.forEach(m -> {
        	SoftwareParadigm sp = new SoftwareParadigm((int) m.get("ID"), (String) m.get("DESCRIPTION"));
        	
        	spList.add(sp);
        });
		
        mnv.addObject("softwareParadigmList", spList);
        
        list = jdbcTemplate.queryForList("SELECT * FROM SUBJECT_AREA");
        
        List<SubjectArea> saList = new ArrayList<>();
        list.forEach(m -> {
        	SubjectArea sa = new SubjectArea((int) m.get("ID"), (String) m.get("DESCRIPTION"));
        	
        	saList.add(sa);
        });
		
        mnv.addObject("subjectAreaList", saList);
		
		return mnv;
	}
	
	@RequestMapping(path = "/security-acquisition/msr/manage", method = {RequestMethod.POST})
	public ModelAndView manageSoftwareFeatureSecurityRequirementView(@ModelAttribute("securityRequirementManager") SecurityRequirementManager securityRequirementManager, 
			@RequestParam(name = "action", defaultValue = "init", required = false) String action) {
		ModelAndView mnv = new ModelAndView("msr-manage");
		
		if(action.contentEquals("add"))
		{
			if(securityRequirementManager.getSecurityRequirementNewSecurityRequirment().contentEquals("N"))
			{
				if(securityRequirementManager.getSecurityRequirementDescription().trim().isEmpty())
				{
					mnv.addObject(STATUS, "0");
	    			mnv.addObject(MESSAGE, "Security Requirement Name may not be empty.");
				}
				else
				{
					int numOfRecords = jdbcTemplate.queryForObject(
	    					"SELECT COUNT(*) AS NUMOFRECORDS FROM [SSKMS].[dbo].[SECURITY_REQUIREMENT] WHERE CAST(DESCRIPTION AS varchar(MAX)) = ?",
	    					(rs, rowNum) -> rs.getInt("NUMOFRECORDS"), securityRequirementManager.getSecurityRequirementDescription());

	    			if (numOfRecords > 0) 
	    			{
	    				mnv.addObject(STATUS, "0");
	    				mnv.addObject(MESSAGE, "Security Requirement '" + securityRequirementManager.getSecurityRequirementDescription() + "' already exists.");
	    			}
	    			else
	    			{
	    				int rows = jdbcTemplate.update("INSERT INTO [dbo].[SECURITY_REQUIREMENT] ([DESCRIPTION]) VALUES (?)", securityRequirementManager.getSecurityRequirementDescription());

	    				if (rows == 1)
	    				{
	    					int securityRequirementId = jdbcTemplate.queryForObject(
	    	    					"SELECT ID FROM [SSKMS].[dbo].[SECURITY_REQUIREMENT] WHERE CAST(DESCRIPTION AS varchar(MAX)) = ?",
	    	    					(rs, rowNum) -> rs.getInt("ID"), securityRequirementManager.getSecurityRequirementDescription());
	    					
	    					rows = jdbcTemplate.update("INSERT INTO [dbo].[SOFTWARE_FEATURE_HAS_SECURITY_REQUIREMENT] ([SOFTWARE_FEATURE_ID], [SECURITY_REQUIREMENT_ID]) VALUES (?, ?)", securityRequirementManager.getSoftwareFeatureId(), securityRequirementId);
	    					
	    					if (rows == 1)
		    				{
		    					mnv.addObject(STATUS, "1");
		    					mnv.addObject(MESSAGE,
		    							"Security Requirement '" + securityRequirementManager.getSecurityRequirementDescription() + "' added successfully.");
		    				}
	    					else
	    					{
	    						mnv.addObject(STATUS, "0");
		    					mnv.addObject(MESSAGE,
		    							"Error occurred while associating Security Requirement '" + securityRequirementManager.getSecurityRequirementDescription() + "' to the selected Software Feature.");
	    					}
	    				}
	    				else
	    				{
	    					mnv.addObject(STATUS, "0");
	    					mnv.addObject(MESSAGE,
	    							"Error occurred while adding Security Requirement '" + securityRequirementManager.getSecurityRequirementDescription() + "'.");
	    				}
	    			}
				}
			}
			else if(securityRequirementManager.getSecurityRequirementNewSecurityRequirment().contentEquals("E"))
			{
				if(securityRequirementManager.getSecurityRequirementId().trim().isEmpty() || securityRequirementManager.getSecurityRequirementId().contentEquals("-1"))
				{
					mnv.addObject(STATUS, "0");
    				mnv.addObject(MESSAGE, "Security Requirement should be selected.");
				}
				else
				{
					int rows = jdbcTemplate.update("INSERT INTO [dbo].[SOFTWARE_FEATURE_HAS_SECURITY_REQUIREMENT] ([SOFTWARE_FEATURE_ID], [SECURITY_REQUIREMENT_ID]) VALUES (?, ?)", securityRequirementManager.getSoftwareFeatureId(), securityRequirementManager.getSecurityRequirementId());
					
					if (rows == 1)
    				{
    					mnv.addObject(STATUS, "1");
    					mnv.addObject(MESSAGE,
    							"Security Requirement '" + securityRequirementManager.getSecurityRequirementDescription() + "' added successfully.");
    				}
					else
					{
						mnv.addObject(STATUS, "0");
    					mnv.addObject(MESSAGE,
    							"Error occurred while associating Security Requirement '" + securityRequirementManager.getSecurityRequirementDescription() + "' to the selected Software Feature.");
					}
				}
			}
		}
		else if(action.contentEquals("delete"))
		{
			int rows = jdbcTemplate.update("DELETE FROM [dbo].[SOFTWARE_FEATURE_HAS_SECURITY_REQUIREMENT] WHERE SOFTWARE_FEATURE_ID = ? AND SECURITY_REQUIREMENT_ID = ?", 
					securityRequirementManager.getSoftwareFeatureId(), securityRequirementManager.getSecurityRequirementId());
			
			if (rows == 1)
			{
				mnv.addObject(STATUS, "1");
				mnv.addObject(MESSAGE,
						"Security Requirement removed successfully.");
			}
			else
			{
				mnv.addObject(STATUS, "0");
				mnv.addObject(MESSAGE,
						"Error occurred while removing Security Requirement.");
			}
		}
		
		SoftwareParadigm sp = jdbcTemplate.queryForObject("SELECT [ID],[DESCRIPTION] FROM [SSKMS].[dbo].[SOFTWARE_PARADIGM] WHERE ID = ?", (rs, rowNum) -> new SoftwareParadigm(rs.getInt("ID"), rs.getString("DESCRIPTION")), Integer.parseInt(securityRequirementManager.getSoftwareParadigmId()));
		SubjectArea sa = jdbcTemplate.queryForObject("SELECT [ID],[DESCRIPTION] FROM [SSKMS].[dbo].[SUBJECT_AREA] WHERE ID = ?", (rs, rowNum) -> new SubjectArea(rs.getInt("ID"), rs.getString("DESCRIPTION")), Integer.parseInt(securityRequirementManager.getSubjectAreaId()));		
		SoftwareFeature sf = jdbcTemplate.queryForObject("SELECT [ID],[DESCRIPTION] FROM [SSKMS].[dbo].[SOFTWARE_FEATURE] WHERE ID = ?", (rs, rowNum) -> new SoftwareFeature(rs.getInt("ID"), rs.getString("DESCRIPTION")), securityRequirementManager.getSoftwareFeatureId());
		
		mnv.addObject("softwareParadigm", sp);
		mnv.addObject("subjectArea", sa);
		mnv.addObject("softwareFeature", sf);
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT SR.ID AS 'SECURITY REQUIREMENT ID', SR.DESCRIPTION AS 'SECURITY REQUIREMENT DESCRIPTION' "
		+ "FROM [SSKMS].[dbo].[SOFTWARE_FEATURE_HAS_SECURITY_REQUIREMENT] AS SFHSR "
		+ "INNER JOIN SOFTWARE_FEATURE SF ON SF.ID = SFHSR.SOFTWARE_FEATURE_ID "
		+ "INNER JOIN SECURITY_REQUIREMENT SR ON SR.ID = SFHSR.SECURITY_REQUIREMENT_ID "
		+ "WHERE SFHSR.SOFTWARE_FEATURE_ID = ?", securityRequirementManager.getSoftwareFeatureId());
		
		List<SecurityRequirement> srList = new ArrayList<>();
		
		list.forEach(m -> {
			SecurityRequirement sr = new SecurityRequirement("" + (int) m.get("SECURITY REQUIREMENT ID"), (String) m.get("SECURITY REQUIREMENT DESCRIPTION")); 
			srList.add(sr);
		});
		
		mnv.addObject("result", srList);
		
		mnv.addObject("securityRequirementManager", new SecurityRequirementManager("" + sf.getId(), sf.getDescription(), "" + sp.getId(), sp.getDescription(), "" + sa.getId(), sa.getDescription(), "-1", "", "E"));
		
		list = jdbcTemplate.queryForList("SELECT SR.ID, SR.DESCRIPTION "
		+ "FROM SECURITY_REQUIREMENT AS SR "
		+ "WHERE SR.ID NOT IN (SELECT SFHSR.SECURITY_REQUIREMENT_ID FROM SOFTWARE_FEATURE_HAS_SECURITY_REQUIREMENT AS SFHSR WHERE SFHSR.SOFTWARE_FEATURE_ID = ?)", securityRequirementManager.getSoftwareFeatureId());
		
		List<SecurityRequirement> srList2 = new ArrayList<>();
		
		list.forEach(m -> {
			SecurityRequirement sr = new SecurityRequirement("" + (int) m.get("ID"), (String) m.get("DESCRIPTION")); 
			srList2.add(sr);
		});
		
		mnv.addObject("securityRequirmentList", srList2);
		
		return mnv;
	}
	
	@RequestMapping(path = "/security-acquisition/mse/manage", method = {RequestMethod.POST})
	public ModelAndView manageSecurityRequirementSecurityErrorView(@ModelAttribute("securityErrorManager") SecurityErrorManager securityErrorManager, 
			@RequestParam(name = "action", defaultValue = "init", required = false) String action) {
		ModelAndView mnv = new ModelAndView("mse-manage");
		
		if(action.contentEquals("add"))
		{
			if(securityErrorManager.getSecurityErrorNewSecurityRequirment().contentEquals("N"))
			{
				if(securityErrorManager.getSecurityErrorDescription().trim().isEmpty())
				{
					mnv.addObject(STATUS, "0");
	    			mnv.addObject(MESSAGE, "Security Error Name may not be empty.");
				}
				else
				{
					int numOfRecords = jdbcTemplate.queryForObject(
	    					"SELECT COUNT(*) AS NUMOFRECORDS FROM [SSKMS].[dbo].[SECURITY_ERROR] WHERE CAST(DESCRIPTION AS varchar(MAX)) = ?",
	    					(rs, rowNum) -> rs.getInt("NUMOFRECORDS"), securityErrorManager.getSecurityErrorDescription());

	    			if (numOfRecords > 0) 
	    			{
	    				mnv.addObject(STATUS, "0");
	    				mnv.addObject(MESSAGE, "Security Error '" + securityErrorManager.getSecurityErrorDescription() + "' already exists.");
	    			}
	    			else
	    			{
	    				int rows = jdbcTemplate.update("INSERT INTO [dbo].[SECURITY_ERROR] ([DESCRIPTION]) VALUES (?)", securityErrorManager.getSecurityErrorDescription());

	    				if (rows == 1)
	    				{
	    					int securityErrorId = jdbcTemplate.queryForObject(
	    	    					"SELECT ID FROM [SSKMS].[dbo].[SECURITY_ERROR] WHERE CAST(DESCRIPTION AS varchar(MAX)) = ?",
	    	    					(rs, rowNum) -> rs.getInt("ID"), securityErrorManager.getSecurityErrorDescription());
	    					
	    					rows = jdbcTemplate.update("INSERT INTO [dbo].[SECURITY_REQUIREMENT_ASSOCIATED_SECURITY_ERROR] ([SECURITY_REQUIREMENT_ID], [SECURITY_ERROR_ID]) VALUES (?, ?)", securityErrorManager.getSecurityRequirementId(), securityErrorId);
	    					
	    					if (rows == 1)
		    				{
		    					mnv.addObject(STATUS, "1");
		    					mnv.addObject(MESSAGE,
		    							"Security Error '" + securityErrorManager.getSecurityErrorDescription() + "' added successfully.");
		    				}
	    					else
	    					{
	    						mnv.addObject(STATUS, "0");
		    					mnv.addObject(MESSAGE,
		    							"Error occurred while associating Security Error '" + securityErrorManager.getSecurityErrorDescription() + "' to the selected Security Requirement.");
	    					}
	    				}
	    				else
	    				{
	    					mnv.addObject(STATUS, "0");
	    					mnv.addObject(MESSAGE,
	    							"Error occurred while adding Security Error '" + securityErrorManager.getSecurityErrorDescription() + "'.");
	    				}
	    			}
				}
			}
			else if(securityErrorManager.getSecurityErrorNewSecurityRequirment().contentEquals("E"))
			{
				if(securityErrorManager.getSecurityErrorId().trim().isEmpty() || securityErrorManager.getSecurityErrorId().contentEquals("-1"))
				{
					mnv.addObject(STATUS, "0");
    				mnv.addObject(MESSAGE, "Security Error should be selected.");
				}
				else
				{
					int rows = jdbcTemplate.update("INSERT INTO [dbo].[SECURITY_REQUIREMENT_ASSOCIATED_SECURITY_ERROR] ([SECURITY_REQUIREMENT_ID], [SECURITY_ERROR_ID]) VALUES (?, ?)", securityErrorManager.getSecurityRequirementId(), securityErrorManager.getSecurityErrorId());
					
					if (rows == 1)
    				{
    					mnv.addObject(STATUS, "1");
    					mnv.addObject(MESSAGE,
    							"Security Error '" + securityErrorManager.getSecurityErrorDescription() + "' added successfully.");
    				}
					else
					{
						mnv.addObject(STATUS, "0");
    					mnv.addObject(MESSAGE,
    							"Error occurred while associating Security Error '" + securityErrorManager.getSecurityErrorDescription() + "' to the selected Security Requirement.");
					}
				}
			}
		}
		else if(action.contentEquals("delete"))
		{
			int rows = jdbcTemplate.update("DELETE FROM [dbo].[SECURITY_REQUIREMENT_ASSOCIATED_SECURITY_ERROR] WHERE SECURITY_REQUIREMENT_ID = ? AND SECURITY_ERROR_ID = ?", 
					securityErrorManager.getSecurityRequirementId(), securityErrorManager.getSecurityErrorId());
			
			if (rows == 1)
			{
				mnv.addObject(STATUS, "1");
				mnv.addObject(MESSAGE,
						"Security Error removed successfully.");
			}
			else
			{
				mnv.addObject(STATUS, "0");
				mnv.addObject(MESSAGE,
						"Error occurred while removing Security Error.");
			}
		}
		
		SecurityRequirement sr = jdbcTemplate.queryForObject("SELECT [ID],[DESCRIPTION] FROM [SSKMS].[dbo].[SECURITY_REQUIREMENT] WHERE ID = ?", (rs, rowNum) -> new SecurityRequirement("" + rs.getInt("ID"), rs.getString("DESCRIPTION")), Integer.parseInt(securityErrorManager.getSecurityRequirementId()));
		
		mnv.addObject("securityRequirement", sr);
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT SE.ID AS 'SECURITY ERROR ID', SE.DESCRIPTION AS 'SECURITY ERROR DESCRIPTION' "
		+ "FROM [SSKMS].[dbo].[SECURITY_REQUIREMENT_ASSOCIATED_SECURITY_ERROR] AS SRASE "
		+ "INNER JOIN SECURITY_REQUIREMENT SR ON SR.ID = SRASE.SECURITY_REQUIREMENT_ID "
		+ "INNER JOIN SECURITY_ERROR SE ON SE.ID = SRASE.SECURITY_ERROR_ID "
		+ "WHERE SRASE.SECURITY_REQUIREMENT_ID = ?", securityErrorManager.getSecurityRequirementId());
		
		List<SecurityError> seList = new ArrayList<>();
		
		list.forEach(m -> {
			SecurityError se = new SecurityError("" + (int) m.get("SECURITY ERROR ID"), (String) m.get("SECURITY ERROR DESCRIPTION")); 
			seList.add(se);
		});
		
		mnv.addObject("result", seList);
		
		mnv.addObject("securityErrorManager", new SecurityErrorManager("" + sr.getId(), sr.getDescription(), "-1", "", "E"));
		
		list = jdbcTemplate.queryForList("SELECT SE.ID, SE.DESCRIPTION "
		+ "FROM SECURITY_ERROR AS SE "
		+ "WHERE SE.ID NOT IN (SELECT SRASE.SECURITY_ERROR_ID FROM SECURITY_REQUIREMENT_ASSOCIATED_SECURITY_ERROR AS SRASE WHERE SRASE.SECURITY_REQUIREMENT_ID = ?)", securityErrorManager.getSecurityRequirementId());
		
		List<SecurityError> seList2 = new ArrayList<>();
		
		list.forEach(m -> {
			SecurityError se = new SecurityError("" + (int) m.get("ID"), (String) m.get("DESCRIPTION")); 
			seList2.add(se);
		});
		
		mnv.addObject("securityErrorList", seList2);
		
		return mnv;
	}
	
	@RequestMapping(path = "/security-acquisition/msw/manage", method = {RequestMethod.POST})
	public ModelAndView manageSecurityErrorSoftwareWeaknessView(@ModelAttribute("softwareWeaknessManager") SoftwareWeaknessManager softwareWeaknessManager, 
			@RequestParam(name = "action", defaultValue = "init", required = false) String action) {
		ModelAndView mnv = new ModelAndView("msw-manage");
		
		if(action.contentEquals("add"))
		{
			if(softwareWeaknessManager.getSoftwareWeaknessNewSoftwareWeakness().contentEquals("N"))
			{
				if(softwareWeaknessManager.getSoftwareWeaknessDescription().trim().isEmpty())
				{
					mnv.addObject(STATUS, "0");
	    			mnv.addObject(MESSAGE, "Software Weakness Name may not be empty.");
				}
				else
				{
					int numOfRecords = jdbcTemplate.queryForObject(
	    					"SELECT COUNT(*) AS NUMOFRECORDS FROM [SSKMS].[dbo].[SOFTWARE_WEAKNESS] WHERE CAST(DESCRIPTION AS varchar(MAX)) = ?",
	    					(rs, rowNum) -> rs.getInt("NUMOFRECORDS"), softwareWeaknessManager.getSoftwareWeaknessDescription());

	    			if (numOfRecords > 0) 
	    			{
	    				mnv.addObject(STATUS, "0");
	    				mnv.addObject(MESSAGE, "Software Weakness '" + softwareWeaknessManager.getSoftwareWeaknessDescription() + "' already exists.");
	    			}
	    			else
	    			{
	    				int rows = jdbcTemplate.update("INSERT INTO [dbo].[SOFTWARE_WEAKNESS] ([DESCRIPTION]) VALUES (?)", softwareWeaknessManager.getSoftwareWeaknessDescription());

	    				if (rows == 1)
	    				{
	    					int softwareWeaknessId = jdbcTemplate.queryForObject(
	    	    					"SELECT ID FROM [SSKMS].[dbo].[SOFTWARE_WEAKNESS] WHERE CAST(DESCRIPTION AS varchar(MAX)) = ?",
	    	    					(rs, rowNum) -> rs.getInt("ID"), softwareWeaknessManager.getSoftwareWeaknessDescription());
	    					
	    					rows = jdbcTemplate.update("INSERT INTO [dbo].[SECURITY_ERROR_CAUSES_SOFTWARE_WEAKNESS] ([SECURITY_ERROR_ID], [SOFTWARE_WEAKNESS_ID]) VALUES (?, ?)", softwareWeaknessManager.getSecurityErrorId(), softwareWeaknessId);
	    					
	    					if (rows == 1)
		    				{
		    					mnv.addObject(STATUS, "1");
		    					mnv.addObject(MESSAGE,
		    							"Software Weakness '" + softwareWeaknessManager.getSoftwareWeaknessDescription() + "' added successfully.");
		    				}
	    					else
	    					{
	    						mnv.addObject(STATUS, "0");
		    					mnv.addObject(MESSAGE,
		    							"Error occurred while associating Software Weakness '" + softwareWeaknessManager.getSoftwareWeaknessDescription() + "' to the selected Security Error.");
	    					}
	    				}
	    				else
	    				{
	    					mnv.addObject(STATUS, "0");
	    					mnv.addObject(MESSAGE,
	    							"Error occurred while adding Software Weakness '" + softwareWeaknessManager.getSoftwareWeaknessDescription() + "'.");
	    				}
	    			}
				}
			}
			else if(softwareWeaknessManager.getSoftwareWeaknessNewSoftwareWeakness().contentEquals("E"))
			{
				if(softwareWeaknessManager.getSoftwareWeaknessId().trim().isEmpty() || softwareWeaknessManager.getSoftwareWeaknessId().contentEquals("-1"))
				{
					mnv.addObject(STATUS, "0");
    				mnv.addObject(MESSAGE, "Software Weakness should be selected.");
				}
				else
				{
					int rows = jdbcTemplate.update("INSERT INTO [dbo].[SECURITY_ERROR_CAUSES_SOFTWARE_WEAKNESS] ([SECURITY_ERROR_ID], [SOFTWARE_WEAKNESS_ID]) VALUES (?, ?)", softwareWeaknessManager.getSecurityErrorId(), softwareWeaknessManager.getSoftwareWeaknessId());
					
					if (rows == 1)
    				{
    					mnv.addObject(STATUS, "1");
    					mnv.addObject(MESSAGE,
    							"Software Weakness '" + softwareWeaknessManager.getSoftwareWeaknessDescription() + "' added successfully.");
    				}
					else
					{
						mnv.addObject(STATUS, "0");
    					mnv.addObject(MESSAGE,
    							"Error occurred while associating Software Weakness '" + softwareWeaknessManager.getSoftwareWeaknessDescription() + "' to the selected Security Error.");
					}
				}
			}
		}
		else if(action.contentEquals("delete"))
		{
			int rows = jdbcTemplate.update("DELETE FROM [dbo].[SECURITY_ERROR_CAUSES_SOFTWARE_WEAKNESS] WHERE SECURITY_ERROR_ID = ? AND SOFTWARE_WEAKNESS_ID = ?", 
					softwareWeaknessManager.getSecurityErrorId(), softwareWeaknessManager.getSoftwareWeaknessId());
			
			if (rows == 1)
			{
				mnv.addObject(STATUS, "1");
				mnv.addObject(MESSAGE,
						"Software Weakness removed successfully.");
			}
			else
			{
				mnv.addObject(STATUS, "0");
				mnv.addObject(MESSAGE,
						"Error occurred while removing Software Weakness.");
			}
		}
		
		SecurityError se = jdbcTemplate.queryForObject("SELECT [ID],[DESCRIPTION] FROM [SSKMS].[dbo].[SECURITY_ERROR] WHERE ID = ?", (rs, rowNum) -> new SecurityError("" + rs.getInt("ID"), rs.getString("DESCRIPTION")), Integer.parseInt(softwareWeaknessManager.getSecurityErrorId()));
		
		mnv.addObject("securityError", se);
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT SW.ID AS 'SOFTWARE WEAKNESS ID', SW.DESCRIPTION AS 'SOFTWARE WEAKNESS DESCRIPTION' "
		+ "FROM [SSKMS].[dbo].[SECURITY_ERROR_CAUSES_SOFTWARE_WEAKNESS] AS SECSW "
		+ "INNER JOIN SECURITY_ERROR SE ON SE.ID = SECSW.SECURITY_ERROR_ID "
		+ "INNER JOIN SOFTWARE_WEAKNESS SW ON SW.ID = SECSW.SOFTWARE_WEAKNESS_ID "
		+ "WHERE SECSW.SECURITY_ERROR_ID = ?", softwareWeaknessManager.getSecurityErrorId());
		
		List<SoftwareWeakness> swList = new ArrayList<>();
		
		list.forEach(m -> {
			SoftwareWeakness sw = new SoftwareWeakness("" + (int) m.get("SOFTWARE WEAKNESS ID"), (String) m.get("SOFTWARE WEAKNESS DESCRIPTION")); 
			swList.add(sw);
		});
		
		mnv.addObject("result", swList);
		
		mnv.addObject("softwareWeaknessManager", new SoftwareWeaknessManager("" + se.getId(), se.getDescription(), "-1", "", "E"));
		
		list = jdbcTemplate.queryForList("SELECT SW.ID, SW.DESCRIPTION "
		+ "FROM SOFTWARE_WEAKNESS AS SW "
		+ "WHERE SW.ID NOT IN (SELECT SECSW.SOFTWARE_WEAKNESS_ID FROM SECURITY_ERROR_CAUSES_SOFTWARE_WEAKNESS AS SECSW WHERE SECSW.SECURITY_ERROR_ID = ?)", softwareWeaknessManager.getSecurityErrorId());
		
		List<SoftwareWeakness> seList2 = new ArrayList<>();
		
		list.forEach(m -> {
			SoftwareWeakness sw = new SoftwareWeakness("" + (int) m.get("ID"), (String) m.get("DESCRIPTION")); 
			seList2.add(sw);
		});
		
		mnv.addObject("softwareWeaknessList", seList2);
		
		return mnv;
	}

	@RequestMapping(path = "/security-acquisition/mcp", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView manageContructionPracticeView(@ModelAttribute("constructionPracticeManager") ConstructionPracticeManager constructionPracticeManager, HttpServletRequest request) {
		
		ModelAndView mnv = new ModelAndView("mcp");
		
		List<SecurityRequirement> srList = new ArrayList<>();	
		mnv.addObject("srList", srList);
		
		List<SecurityError> seList = new ArrayList<>();		
		mnv.addObject("seList", seList);
		
		if (request.getMethod().equals(RequestMethod.POST.name()))
		{			
			if(constructionPracticeManager.getSoftwareFeatureId().contentEquals("-1") || constructionPracticeManager.getSoftwareFeatureId().trim().isEmpty())
			{
				mnv.addObject(STATUS, "0");
    			mnv.addObject(MESSAGE, "Select Software Feature.");
			}
			else if(constructionPracticeManager.getSecurityRequirementId().contentEquals("-1") || constructionPracticeManager.getSecurityRequirementId().trim().isEmpty())
			{
				mnv.addObject(STATUS, "0");
    			mnv.addObject(MESSAGE, "Select Security Requirement.");
    			
    			constructionPracticeManager.setSoftwareFeatureId("");
			}
			else if(constructionPracticeManager.getSecurityErrorId().contentEquals("-1") || constructionPracticeManager.getSecurityErrorId().trim().isEmpty())
			{
				mnv.addObject(STATUS, "0");
    			mnv.addObject(MESSAGE, "Select Security Error.");
    			
    			constructionPracticeManager.setSoftwareFeatureId("");
    			constructionPracticeManager.setSecurityRequirementId("");
			}
			else
			{				
				if(constructionPracticeManager.getSelectConstructionPractice().contentEquals("E"))
				{
					if(constructionPracticeManager.getConstructionPracticeId().contentEquals("-1") || constructionPracticeManager.getConstructionPracticeId().trim().isEmpty())
					{
						mnv.addObject(STATUS, "0");
		    			mnv.addObject(MESSAGE, "Select an existing Construction Practice.");
					}
					else
					{
						int rows;
						int count;
						
						boolean error = false;
						
						count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM [dbo].[SOFTWARE_FEATURE_HAS_CONSTRUCTION_PRACTICE] WHERE [SOFTWARE_FEATURE_ID] = ? AND [CONSTRUCTION_PRACTICE_ID] = ?", Integer.class, constructionPracticeManager.getSoftwareFeatureId(), constructionPracticeManager.getConstructionPracticeId());
						
						if(count == 0)
						{
							rows = jdbcTemplate.update("INSERT INTO [dbo].[SOFTWARE_FEATURE_HAS_CONSTRUCTION_PRACTICE] ([SOFTWARE_FEATURE_ID], [CONSTRUCTION_PRACTICE_ID]) VALUES (?, ?)", constructionPracticeManager.getSoftwareFeatureId(), constructionPracticeManager.getConstructionPracticeId());
							
							if(rows != 1)
							{
								error = true;
							}
						}
						
						count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM [dbo].[SECURITY_REQUIREMENT_FOLLOWED_BY_CONSTRUCTION_PRACTICE] WHERE [SECURITY_REQUIREMENT_ID] = ? AND [CONSTRUCTION_PRACTICE_ID] = ?", Integer.class, constructionPracticeManager.getSecurityRequirementId(), constructionPracticeManager.getConstructionPracticeId());
						
						if(count == 0)
						{
							rows = jdbcTemplate.update("INSERT INTO [dbo].[SECURITY_REQUIREMENT_FOLLOWED_BY_CONSTRUCTION_PRACTICE] ([SECURITY_REQUIREMENT_ID], [CONSTRUCTION_PRACTICE_ID]) VALUES (?, ?)", constructionPracticeManager.getSecurityRequirementId(), constructionPracticeManager.getConstructionPracticeId());
							
							if(rows != 1)
							{
								error = true;
							}
						}
						
						count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM [dbo].[SECURITY_ERROR_MITIGATED_BY_CONSTRUCTION_PRACTICE] WHERE [SECURITY_ERROR_ID] = ? AND [CONSTRUCTION_PRACTICE_ID] = ?", Integer.class, constructionPracticeManager.getSecurityErrorId(), constructionPracticeManager.getConstructionPracticeId());

						if(count == 0)
						{
							rows = jdbcTemplate.update("INSERT INTO [dbo].[SECURITY_ERROR_MITIGATED_BY_CONSTRUCTION_PRACTICE] ([SECURITY_ERROR_ID], [CONSTRUCTION_PRACTICE_ID]) VALUES (?, ?)", constructionPracticeManager.getSecurityErrorId(), constructionPracticeManager.getConstructionPracticeId());
							
							if(rows != 1)
							{
								error = true;
							}
						}
						
						if(!error)
						{
							mnv.addObject(STATUS, "1");
			    			mnv.addObject(MESSAGE, "Construction Practice inserted successfully.");
						}
						else
						{
							mnv.addObject(STATUS, "0");
			    			mnv.addObject(MESSAGE, "An error occurred while inserting the Construction Practice.");
						}
					}
				}
				else if(constructionPracticeManager.getSelectConstructionPractice().contentEquals("N"))
				{
					if(constructionPracticeManager.getConstructionPracticeDescription().trim().isEmpty())
					{
						mnv.addObject(STATUS, "0");
		    			mnv.addObject(MESSAGE, "Construction Practice description may not be empty.");
					}
					else if(constructionPracticeManager.getFollowStrategy() == null && constructionPracticeManager.getHasMethod() == null)
					{
						mnv.addObject(STATUS, "0");
		    			mnv.addObject(MESSAGE, "Either a strategy and/or a method should be provided.");
					}
					else if(constructionPracticeManager.getFollowStrategy() != null && constructionPracticeManager.getStrategyDescription().trim().isEmpty())
					{
						mnv.addObject(STATUS, "0");
		    			mnv.addObject(MESSAGE, "Strategy description may not be empty.");
					}
					else if(constructionPracticeManager.getHasMethod() != null && (constructionPracticeManager.getMethodDetails().trim().isEmpty()))
					{
						mnv.addObject(STATUS, "0");
		    			mnv.addObject(MESSAGE, "Method details may not be empty.");
					}
					else if(constructionPracticeManager.getRelatedLanguage() != null && constructionPracticeManager.getLanguage().trim().isEmpty())
					{
						mnv.addObject(STATUS, "0");
		    			mnv.addObject(MESSAGE, "Language may not be empty.");
					}
					else if(constructionPracticeManager.getHasMechanism() != null && constructionPracticeManager.getMechanism().trim().isEmpty())
					{
						mnv.addObject(STATUS, "0");
		    			mnv.addObject(MESSAGE, "Mechanism may not be empty.");
					}
					else if(constructionPracticeManager.getMechanismUtilizesSecurityTool() != null && constructionPracticeManager.getSecurityTool().trim().isEmpty())
					{
						mnv.addObject(STATUS, "0");
		    			mnv.addObject(MESSAGE, "Security Tool may not be empty.");
					}
					else
					{
						boolean error = false;
						
						int rows;
						int count;
						
						int cpId;
						
						String commonInsertStat = "DECLARE @INSERTED_TABLE TABLE (ID INT);";
						
						try
						{
							cpId = jdbcTemplate.queryForObject("SELECT ID FROM [dbo].[CONSTRUCTION_PRACTICE] WHERE [DESCRIPTION] LIKE CAST(? AS VARCHAR(MAX))", Integer.class, constructionPracticeManager.getConstructionPracticeDescription());
						}
						catch(EmptyResultDataAccessException ex)
						{
							String cpInsert = commonInsertStat
									+ "DECLARE @CONSTRUCTION_PRACTICE_ID INT;"
									+ "INSERT INTO [dbo].[CONSTRUCTION_PRACTICE] ([DESCRIPTION]) OUTPUT INSERTED.ID INTO @INSERTED_TABLE VALUES (?);"
									+ "SELECT @CONSTRUCTION_PRACTICE_ID = ID FROM @INSERTED_TABLE;"
									+ "SELECT @CONSTRUCTION_PRACTICE_ID AS 'CONSTRUCTION PRACTICE ID';";
					
							cpId = jdbcTemplate.queryForObject(cpInsert, (rs, rowNum) -> rs.getInt("CONSTRUCTION PRACTICE ID"), constructionPracticeManager.getConstructionPracticeDescription());
						}
						
						if(constructionPracticeManager.getFollowStrategy() != null)
						{
							int fsId;
							
							try
							{
								fsId = jdbcTemplate.queryForObject("SELECT ID FROM [dbo].[STRATEGY] WHERE [DESCRIPTION] LIKE CAST(? AS VARCHAR(MAX))", Integer.class, constructionPracticeManager.getStrategyDescription());
							}
							catch(EmptyResultDataAccessException ex)
							{
								String fsInsert = commonInsertStat
										+ "DECLARE @STRATEGY_ID INT;"
										+ "INSERT INTO [dbo].[STRATEGY] ([DESCRIPTION]) OUTPUT INSERTED.ID INTO @INSERTED_TABLE VALUES (?);"
										+ "SELECT @STRATEGY_ID = ID FROM @INSERTED_TABLE;"
										+ "SELECT @STRATEGY_ID AS 'STRATEGY ID';";
								
								fsId = jdbcTemplate.queryForObject(fsInsert, (rs, rowNum) -> rs.getInt("STRATEGY ID"), constructionPracticeManager.getStrategyDescription());
							}
							
							count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM [dbo].[CONSTRUCTION_PRACTICE_FOLLOWS_STRATEGY] WHERE [CONSTRUCTION_PRACTICE_ID] = ? AND [STRATEGY_ID] = ?", Integer.class, cpId, fsId);

							if(count == 0)
							{
								rows = jdbcTemplate.update("INSERT INTO [dbo].[CONSTRUCTION_PRACTICE_FOLLOWS_STRATEGY] ([CONSTRUCTION_PRACTICE_ID], [STRATEGY_ID]) VALUES (?, ?)", cpId, fsId);
								
								if(rows != 1)
								{
									error = true;
								}
							}
						}
						
						if(constructionPracticeManager.getHasMethod() != null)
						{
							int mId;
							
							String mInsert = commonInsertStat
									+ "DECLARE @METHOD_ID INT;"
									+ "INSERT INTO [dbo].[METHOD] ([DESCRIPTION]) OUTPUT INSERTED.ID INTO @INSERTED_TABLE VALUES (?);"
									+ "SELECT @METHOD_ID = ID FROM @INSERTED_TABLE;" 
									+ "SELECT @METHOD_ID AS 'METHOD ID';";
							
							mId = jdbcTemplate.queryForObject(mInsert, (rs, rowNum) -> rs.getInt("METHOD ID"), constructionPracticeManager.getMethodDetails());
							
							count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM [dbo].[CONSTRUCTION_PRACTICE_HAS_METHOD] WHERE [CONSTRUCTION_PRACTICE_ID] = ? AND [METHOD_ID] = ?", Integer.class, cpId, mId);

							if(count == 0)
							{
								rows = jdbcTemplate.update("INSERT INTO [dbo].[CONSTRUCTION_PRACTICE_HAS_METHOD] ([CONSTRUCTION_PRACTICE_ID], [METHOD_ID]) VALUES (?, ?)", cpId, mId);
								
								if(rows != 1)
								{
									error = true;
								}
							}
							
							if(constructionPracticeManager.getRelatedLanguage() != null)
							{
								int lId;
								
								try
								{
									lId = jdbcTemplate.queryForObject("SELECT ID FROM [dbo].[LANGUAGE] WHERE [DESCRIPTION] LIKE CAST(? AS VARCHAR(MAX))", Integer.class, constructionPracticeManager.getLanguage());
								}
								catch(EmptyResultDataAccessException ex)
								{
									String lInsert = commonInsertStat
											+ "DECLARE @LANGUAGE_ID INT;"
											+ "INSERT INTO [dbo].[LANGUAGE] ([DESCRIPTION]) OUTPUT INSERTED.ID INTO @INSERTED_TABLE VALUES (?);"
											+ "SELECT @LANGUAGE_ID = ID FROM @INSERTED_TABLE;"
											+ "SELECT @LANGUAGE_ID AS 'LANGUAGE ID';";
									
									lId = jdbcTemplate.queryForObject(lInsert, (rs, rowNum) -> rs.getInt("LANGUAGE ID"), constructionPracticeManager.getLanguage());
								}
								
								count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM [dbo].[METHOD_RELATED_LANGUAGE] WHERE [METHOD_ID] = ? AND [LANGUAGE_ID] = ?", Integer.class, mId, lId);
								
								if(count == 0)
								{
									rows = jdbcTemplate.update("INSERT INTO [dbo].[METHOD_RELATED_LANGUAGE] ([METHOD_ID], [LANGUAGE_ID]) VALUES (?, ?)", mId, lId);
									
									if(rows != 1)
									{
										error = true;
									}
								}
							}
							
							if(constructionPracticeManager.getHasMechanism() != null)
							{
								int mechId;
								
								try
								{
									mechId = jdbcTemplate.queryForObject("SELECT ID FROM [dbo].[MECHANISM] WHERE [DESCRIPTION] LIKE CAST(? AS VARCHAR(MAX))", Integer.class, constructionPracticeManager.getMechanism());
								}
								catch(EmptyResultDataAccessException ex)
								{
									String mechInsert = commonInsertStat
											+ "DECLARE @MECHANISM_ID INT;"
											+ "INSERT INTO [dbo].[MECHANISM] ([DESCRIPTION]) OUTPUT INSERTED.ID INTO @INSERTED_TABLE VALUES (?);"
											+ "SELECT @MECHANISM_ID = ID FROM @INSERTED_TABLE;"
											+ "SELECT @MECHANISM_ID AS 'MECHANISM ID';";
									
									mechId = jdbcTemplate.queryForObject(mechInsert, (rs, rowNum) -> rs.getInt("MECHANISM ID"), constructionPracticeManager.getMechanism());
								}
								
								count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM [dbo].[METHOD_HAS_MECHANISM] WHERE [METHOD_ID] = ? AND [MECHANISM_ID] = ?", Integer.class, mId, mechId);
								
								if(count == 0)
								{
									rows = jdbcTemplate.update("INSERT INTO [dbo].[METHOD_HAS_MECHANISM] ([METHOD_ID], [MECHANISM_ID]) VALUES (?, ?)", mId, mechId);
									
									if(rows != 1)
									{
										error = true;
									}
								}
								
								if(constructionPracticeManager.getMechanismUtilizesSecurityTool() != null)
								{
									int sId;
									
									try
									{
										sId = jdbcTemplate.queryForObject("SELECT ID FROM [dbo].[SECURITY_TOOL]  WHERE [DESCRIPTION] LIKE CAST(? AS VARCHAR(MAX))", Integer.class, constructionPracticeManager.getSecurityTool());
									}
									catch(EmptyResultDataAccessException ex)
									{
										String sInsert = commonInsertStat
												+ "DECLARE @SECURITY_TOOL_ID INT;"
												+ "INSERT INTO [dbo].[SECURITY_TOOL] ([DESCRIPTION]) OUTPUT INSERTED.ID INTO @INSERTED_TABLE VALUES (?);"
												+ "SELECT @SECURITY_TOOL_ID = ID FROM @INSERTED_TABLE;"
												+ "SELECT @SECURITY_TOOL_ID AS 'SECURITY TOOL ID';";
										
										sId = jdbcTemplate.queryForObject(sInsert, (rs, rowNum) -> rs.getInt("SECURITY TOOL ID"), constructionPracticeManager.getSecurityTool());
									}
									
									count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM [dbo].[MECHANISM_UTILIZES_SECURITY_TOOL] WHERE [MECHANISM_ID] = ? AND [SECURITY_TOOL_ID] = ?", Integer.class, mechId, sId);
									
									if(count == 0)
									{
										rows = jdbcTemplate.update("INSERT INTO [dbo].[MECHANISM_UTILIZES_SECURITY_TOOL] ([MECHANISM_ID], [SECURITY_TOOL_ID]) VALUES (?, ?)", mechId, sId);
										
										if(rows != 1)
										{
											error = true;
										}
									}
								}
							}
						}
						
						count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM [dbo].[SOFTWARE_FEATURE_HAS_CONSTRUCTION_PRACTICE] WHERE [SOFTWARE_FEATURE_ID] = ? AND [CONSTRUCTION_PRACTICE_ID] = ?", Integer.class, constructionPracticeManager.getSoftwareFeatureId(), cpId);
						
						if(count == 0)
						{
							rows = jdbcTemplate.update("INSERT INTO [dbo].[SOFTWARE_FEATURE_HAS_CONSTRUCTION_PRACTICE] ([SOFTWARE_FEATURE_ID], [CONSTRUCTION_PRACTICE_ID]) VALUES (?, ?)", constructionPracticeManager.getSoftwareFeatureId(), cpId);
							
							if(rows != 1)
							{
								error = true;
							}
						}
						
						count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM [dbo].[SECURITY_REQUIREMENT_FOLLOWED_BY_CONSTRUCTION_PRACTICE] WHERE [SECURITY_REQUIREMENT_ID] = ? AND [CONSTRUCTION_PRACTICE_ID] = ?", Integer.class, constructionPracticeManager.getSecurityRequirementId(), cpId);
						
						if(count == 0)
						{
							rows = jdbcTemplate.update("INSERT INTO [dbo].[SECURITY_REQUIREMENT_FOLLOWED_BY_CONSTRUCTION_PRACTICE] ([SECURITY_REQUIREMENT_ID], [CONSTRUCTION_PRACTICE_ID]) VALUES (?, ?)", constructionPracticeManager.getSecurityRequirementId(), cpId);
							
							if(rows != 1)
							{
								error = true;
							}
						}
						
						count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM [dbo].[SECURITY_ERROR_MITIGATED_BY_CONSTRUCTION_PRACTICE] WHERE [SECURITY_ERROR_ID] = ? AND [CONSTRUCTION_PRACTICE_ID] = ?", Integer.class, constructionPracticeManager.getSecurityErrorId(), cpId);
						
						if(count == 0)
						{
							rows = jdbcTemplate.update("INSERT INTO [dbo].[SECURITY_ERROR_MITIGATED_BY_CONSTRUCTION_PRACTICE] ([SECURITY_ERROR_ID], [CONSTRUCTION_PRACTICE_ID]) VALUES (?, ?)", constructionPracticeManager.getSecurityErrorId(), cpId);
							
							if(rows != 1)
							{
								error = true;
							}
						}
						
						if(!error)
						{
							mnv.addObject(STATUS, "1");
			    			mnv.addObject(MESSAGE, "Construction Practice inserted successfully.");
						}
						else
						{
							mnv.addObject(STATUS, "0");
			    			mnv.addObject(MESSAGE, "An error occurred while inserting the Construction Practice.");
						}
					}
				}
			}
			
			if(!constructionPracticeManager.getSoftwareFeatureId().contentEquals("-1") && !constructionPracticeManager.getSoftwareFeatureId().trim().isEmpty())
			{
				srList = getSecurityRequirementsList(constructionPracticeManager.getSoftwareFeatureId());
				
				mnv.addObject("srList", srList);
			}
			
			if(!constructionPracticeManager.getSecurityRequirementId().contentEquals("-1") && !constructionPracticeManager.getSecurityRequirementId().trim().isEmpty())
			{
				seList = getSecurityErrorsList(constructionPracticeManager.getSecurityRequirementId());
				
				mnv.addObject("seList", seList);
			}
		}
		
		List<Map<String,Object>> list = jdbcTemplate.queryForList("SELECT [ID], [DESCRIPTION] FROM [SSKMS].[dbo].[SOFTWARE_FEATURE]");        
        List<SoftwareFeature> sfList = new ArrayList<>();        
        list.forEach(m -> {               
        	SoftwareFeature sf = new SoftwareFeature((int) m.get("ID"), (String) m.get("DESCRIPTION"));
        	sfList.add(sf);               
        });
        
        mnv.addObject("softwareFeatureList", sfList);
        
        List<Map<String,Object>> list2 = jdbcTemplate.queryForList("SELECT [ID],[DESCRIPTION] FROM [SSKMS].[dbo].[CONSTRUCTION_PRACTICE]");
        
        List<ConstructionPractice> sfList2 = new ArrayList<>();        
        list2.forEach(m -> {               
        	ConstructionPractice cp = new ConstructionPractice((int) m.get("ID"), (String) m.get("DESCRIPTION"));
        	sfList2.add(cp);               
        });
        
        mnv.addObject("constructionPracticeList", sfList2);
        
        if(constructionPracticeManager.getSelectConstructionPractice().contentEquals("E"))
        {
        	constructionPracticeManager.setMethodDetails("");
        }
		
		return mnv;
	}
	
	@RequestMapping(path = "/security-acquisition/mvp", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView manageVerificationPracticeView(@ModelAttribute("verificationPracticeManager") VerificationPracticeManager verificationPracticeManager, HttpServletRequest request) {
		
		ModelAndView mnv = new ModelAndView("mvp");
		
		List<SecurityRequirement> srList = new ArrayList<>();	
		mnv.addObject("srList", srList);
		
		List<SecurityError> seList = new ArrayList<>();		
		mnv.addObject("seList", seList);
		
		if (request.getMethod().equals(RequestMethod.POST.name()))
		{			
			if(verificationPracticeManager.getSoftwareFeatureId().contentEquals("-1") || verificationPracticeManager.getSoftwareFeatureId().trim().isEmpty())
			{
				mnv.addObject(STATUS, "0");
    			mnv.addObject(MESSAGE, "Select Software Feature.");
			}
			else if(verificationPracticeManager.getSecurityRequirementId().contentEquals("-1") || verificationPracticeManager.getSecurityRequirementId().trim().isEmpty())
			{
				mnv.addObject(STATUS, "0");
    			mnv.addObject(MESSAGE, "Select Security Requirement.");
    			
    			verificationPracticeManager.setSoftwareFeatureId("");
			}
			else if(verificationPracticeManager.getSecurityErrorId().contentEquals("-1") || verificationPracticeManager.getSecurityErrorId().trim().isEmpty())
			{
				mnv.addObject(STATUS, "0");
    			mnv.addObject(MESSAGE, "Select Security Error.");
    			
    			verificationPracticeManager.setSoftwareFeatureId("");
    			verificationPracticeManager.setSecurityRequirementId("");
			}
			else
			{				
				if(verificationPracticeManager.getSelectVerificationPractice().contentEquals("E"))
				{
					if(verificationPracticeManager.getVerificationPracticeId().contentEquals("-1") || verificationPracticeManager.getVerificationPracticeId().trim().isEmpty())
					{
						mnv.addObject(STATUS, "0");
		    			mnv.addObject(MESSAGE, "Select an existing Verification Practice.");
					}
					else
					{
						int rows;
						int count;
						
						boolean error = false;
						
						count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM [dbo].[SECURITY_REQUIREMENT_FOLLOWED_BY_VERIFICATION_PRACTICE] WHERE [SECURITY_REQUIREMENT_ID] = ? AND [VERIFICATION_PRACTICE_ID] = ?", Integer.class, verificationPracticeManager.getSecurityRequirementId(), verificationPracticeManager.getVerificationPracticeId());
						
						if(count == 0)
						{
							rows = jdbcTemplate.update("INSERT INTO [dbo].[SECURITY_REQUIREMENT_FOLLOWED_BY_VERIFICATION_PRACTICE] ([SECURITY_REQUIREMENT_ID], [VERIFICATION_PRACTICE_ID]) VALUES (?, ?)", verificationPracticeManager.getSecurityRequirementId(), verificationPracticeManager.getVerificationPracticeId());
							
							if(rows != 1)
							{
								error = true;
							}
						}
						
						count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM [dbo].[SECURITY_ERROR_SPOTTED_BY_VERIFICATION_PRACTICE] WHERE [SECURITY_ERROR_ID] = ? AND [VERIFICATION_PRACTICE_ID] = ?", Integer.class, verificationPracticeManager.getSecurityErrorId(), verificationPracticeManager.getVerificationPracticeId());

						if(count == 0)
						{
							rows = jdbcTemplate.update("INSERT INTO [dbo].[SECURITY_ERROR_SPOTTED_BY_VERIFICATION_PRACTICE] ([SECURITY_ERROR_ID], [VERIFICATION_PRACTICE_ID]) VALUES (?, ?)", verificationPracticeManager.getSecurityErrorId(), verificationPracticeManager.getVerificationPracticeId());
							
							if(rows != 1)
							{
								error = true;
							}
						}
						
						if(!error)
						{
							mnv.addObject(STATUS, "1");
			    			mnv.addObject(MESSAGE, "Verification Practice inserted successfully.");
						}
						else
						{
							mnv.addObject(STATUS, "0");
			    			mnv.addObject(MESSAGE, "An error occurred while inserting the Verification Practice.");
						}
					}
				}
				else if(verificationPracticeManager.getSelectVerificationPractice().contentEquals("N"))
				{
					if(verificationPracticeManager.getVerificationPracticeDescription().trim().isEmpty())
					{
						mnv.addObject(STATUS, "0");
		    			mnv.addObject(MESSAGE, "Verification Practice description may not be empty.");
					}
					else if(verificationPracticeManager.getHasApproach() == null && verificationPracticeManager.getHasTechnique() == null)
					{
						mnv.addObject(STATUS, "0");
		    			mnv.addObject(MESSAGE, "Either an approach and/or a technique should be provided.");
					}
					else if(verificationPracticeManager.getHasApproach() != null && verificationPracticeManager.getApproachDescription().trim().isEmpty())
					{
						mnv.addObject(STATUS, "0");
		    			mnv.addObject(MESSAGE, "Approach description may not be empty.");
					}
					else if(verificationPracticeManager.getHasTechnique() != null && (verificationPracticeManager.getTechniqueDetails().trim().isEmpty()))
					{
						mnv.addObject(STATUS, "0");
		    			mnv.addObject(MESSAGE, "Technique details may not be empty.");
					}
					else if(verificationPracticeManager.getHasSecurityTool() != null && verificationPracticeManager.getSecurityTool().trim().isEmpty())
					{
						mnv.addObject(STATUS, "0");
		    			mnv.addObject(MESSAGE, "Security Tool may not be empty.");
					}
					else
					{
						boolean error = false;
						
						int rows;
						int count;
						
						int vpId;
						
						String commonInsertStat = "DECLARE @INSERTED_TABLE TABLE (ID INT);";
						
						try
						{
							vpId = jdbcTemplate.queryForObject("SELECT ID FROM [dbo].[VERIFICATION_PRACTICE] WHERE [DESCRIPTION] LIKE CAST(? AS VARCHAR(MAX))", Integer.class, verificationPracticeManager.getVerificationPracticeDescription());
						}
						catch(EmptyResultDataAccessException ex)
						{
							String vpInsert = commonInsertStat
									+ "DECLARE @VERIFICATION_PRACTICE_ID INT;"
									+ "INSERT INTO [dbo].[VERIFICATION_PRACTICE] ([DESCRIPTION]) OUTPUT INSERTED.ID INTO @INSERTED_TABLE VALUES (?);"
									+ "SELECT @VERIFICATION_PRACTICE_ID = ID FROM @INSERTED_TABLE;"
									+ "SELECT @VERIFICATION_PRACTICE_ID AS 'VERIFICATION PRACTICE ID';";
					
							vpId = jdbcTemplate.queryForObject(vpInsert, (rs, rowNum) -> rs.getInt("VERIFICATION PRACTICE ID"), verificationPracticeManager.getVerificationPracticeDescription());
						}
						
						if(verificationPracticeManager.getHasApproach() != null)
						{
							int haId;
							
							try
							{
								haId = jdbcTemplate.queryForObject("SELECT ID FROM [dbo].[APPROACH] WHERE [DESCRIPTION] LIKE CAST(? AS VARCHAR(MAX))", Integer.class, verificationPracticeManager.getApproachDescription());
							}
							catch(EmptyResultDataAccessException ex)
							{
								String haInsert = commonInsertStat
										+ "DECLARE @APPROACH_ID INT;"
										+ "INSERT INTO [dbo].[APPROACH] ([DESCRIPTION]) OUTPUT INSERTED.ID INTO @INSERTED_TABLE VALUES (?);"
										+ "SELECT @APPROACH_ID = ID FROM @INSERTED_TABLE;"
										+ "SELECT @APPROACH_ID AS 'APPROACH ID';";
								
								haId = jdbcTemplate.queryForObject(haInsert, (rs, rowNum) -> rs.getInt("APPROACH ID"), verificationPracticeManager.getApproachDescription());
							}
							
							count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM [dbo].[VERIFICATION_PRACTICE_HAS_APPROACH] WHERE [VERIFICATION_PRACTICE_ID] = ? AND [APPROACH_ID] = ?", Integer.class, vpId, haId);

							if(count == 0)
							{
								rows = jdbcTemplate.update("INSERT INTO [dbo].[VERIFICATION_PRACTICE_HAS_APPROACH] ([VERIFICATION_PRACTICE_ID], [APPROACH_ID]) VALUES (?, ?)", vpId, haId);
								
								if(rows != 1)
								{
									error = true;
								}
							}
						}
						
						if(verificationPracticeManager.getHasTechnique() != null)
						{
							int tId;
							
							String tInsert = commonInsertStat
									+ "DECLARE @TECHNIQUE_ID INT;"
									+ "INSERT INTO [dbo].[TECHNIQUE] ([DESCRIPTION]) OUTPUT INSERTED.ID INTO @INSERTED_TABLE VALUES (?);"
									+ "SELECT @TECHNIQUE_ID = ID FROM @INSERTED_TABLE;" 
									+ "SELECT @TECHNIQUE_ID AS 'TECHNIQUE ID';";
							
							tId = jdbcTemplate.queryForObject(tInsert, (rs, rowNum) -> rs.getInt("TECHNIQUE ID"), verificationPracticeManager.getTechniqueDetails());
							
							count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM [dbo].[VERIFICATION_PRACTICE_HAS_TECHNIQUE] WHERE [VERIFICATION_PRACTICE_ID] = ? AND [TECHNIQUE_ID] = ?", Integer.class, vpId, tId);

							if(count == 0)
							{
								rows = jdbcTemplate.update("INSERT INTO [dbo].[VERIFICATION_PRACTICE_HAS_TECHNIQUE] ([VERIFICATION_PRACTICE_ID], [TECHNIQUE_ID]) VALUES (?, ?)", vpId, tId);
								
								if(rows != 1)
								{
									error = true;
								}
							}
							
							if(verificationPracticeManager.getHasSecurityTool() != null)
							{
								int stId;
								
								try
								{
									stId = jdbcTemplate.queryForObject("SELECT ID FROM [dbo].[SECURITY_TOOL] WHERE [DESCRIPTION] LIKE CAST(? AS VARCHAR(MAX))", Integer.class, verificationPracticeManager.getSecurityTool());
								}
								catch(EmptyResultDataAccessException ex)
								{
									String lInsert = commonInsertStat
											+ "DECLARE @SECURITY_TOOL_ID INT;"
											+ "INSERT INTO [dbo].[SECURITY_TOOL] ([DESCRIPTION]) OUTPUT INSERTED.ID INTO @INSERTED_TABLE VALUES (?);"
											+ "SELECT @SECURITY_TOOL_ID = ID FROM @INSERTED_TABLE;"
											+ "SELECT @SECURITY_TOOL_ID AS 'SECURITY TOOL ID';";
									
									stId = jdbcTemplate.queryForObject(lInsert, (rs, rowNum) -> rs.getInt("SECURITY TOOL ID"), verificationPracticeManager.getSecurityTool());
								}
								
								count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM [dbo].[TECHNIQUE_HAS_SECURITY_TOOL] WHERE [TECHNIQUE_ID] = ? AND [SECURITY_TOOL_ID] = ?", Integer.class, tId, stId);
								
								if(count == 0)
								{
									rows = jdbcTemplate.update("INSERT INTO [dbo].[TECHNIQUE_HAS_SECURITY_TOOL] ([TECHNIQUE_ID], [SECURITY_TOOL_ID]) VALUES (?, ?)", tId, stId);
									
									if(rows != 1)
									{
										error = true;
									}
								}
							}
						}
						
						count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM [dbo].[SECURITY_REQUIREMENT_FOLLOWED_BY_VERIFICATION_PRACTICE] WHERE [SECURITY_REQUIREMENT_ID] = ? AND [VERIFICATION_PRACTICE_ID] = ?", Integer.class, verificationPracticeManager.getSecurityRequirementId(), vpId);
						
						if(count == 0)
						{
							rows = jdbcTemplate.update("INSERT INTO [dbo].[SECURITY_REQUIREMENT_FOLLOWED_BY_VERIFICATION_PRACTICE] ([SECURITY_REQUIREMENT_ID], [VERIFICATION_PRACTICE_ID]) VALUES (?, ?)", verificationPracticeManager.getSecurityRequirementId(), vpId);
							
							if(rows != 1)
							{
								error = true;
							}
						}
						
						count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM [dbo].[SECURITY_ERROR_SPOTTED_BY_VERIFICATION_PRACTICE] WHERE [SECURITY_ERROR_ID] = ? AND [VERIFICATION_PRACTICE_ID] = ?", Integer.class, verificationPracticeManager.getSecurityErrorId(), vpId);
						
						if(count == 0)
						{
							rows = jdbcTemplate.update("INSERT INTO [dbo].[SECURITY_ERROR_SPOTTED_BY_VERIFICATION_PRACTICE] ([SECURITY_ERROR_ID], [VERIFICATION_PRACTICE_ID]) VALUES (?, ?)", verificationPracticeManager.getSecurityErrorId(), vpId);
							
							if(rows != 1)
							{
								error = true;
							}
						}
						
						if(!error)
						{
							mnv.addObject(STATUS, "1");
			    			mnv.addObject(MESSAGE, "Verification Practice inserted successfully.");
						}
						else
						{
							mnv.addObject(STATUS, "0");
			    			mnv.addObject(MESSAGE, "An error occurred while inserting the Verification Practice.");
						}
					}
				}
			}
			
			if(!verificationPracticeManager.getSoftwareFeatureId().contentEquals("-1") && !verificationPracticeManager.getSoftwareFeatureId().trim().isEmpty())
			{
				srList = getSecurityRequirementsList(verificationPracticeManager.getSoftwareFeatureId());
				
				mnv.addObject("srList", srList);
			}
			
			if(!verificationPracticeManager.getSecurityRequirementId().contentEquals("-1") && !verificationPracticeManager.getSecurityRequirementId().trim().isEmpty())
			{
				seList = getSecurityErrorsList(verificationPracticeManager.getSecurityRequirementId());
				
				mnv.addObject("seList", seList);
			}
		}
		
		List<Map<String,Object>> list = jdbcTemplate.queryForList("SELECT [ID], [DESCRIPTION] FROM [SSKMS].[dbo].[SOFTWARE_FEATURE]");        
        List<SoftwareFeature> sfList = new ArrayList<>();        
        list.forEach(m -> {               
        	SoftwareFeature sf = new SoftwareFeature((int) m.get("ID"), (String) m.get("DESCRIPTION"));
        	sfList.add(sf);               
        });
        
        mnv.addObject("softwareFeatureList", sfList);
        
        List<Map<String,Object>> list2 = jdbcTemplate.queryForList("SELECT [ID],[DESCRIPTION] FROM [SSKMS].[dbo].[VERIFICATION_PRACTICE]");
        
        List<VerificationPractice> sfList2 = new ArrayList<>();        
        list2.forEach(m -> {               
        	VerificationPractice vp = new VerificationPractice((int) m.get("ID"), (String) m.get("DESCRIPTION"));
        	sfList2.add(vp);               
        });
        
        mnv.addObject("verificationPracticeList", sfList2);
        
        if(verificationPracticeManager.getSelectVerificationPractice().contentEquals("E"))
        {
        	verificationPracticeManager.setTechniqueDetails("");
        }
		
		return mnv;
	}
	
	@RequestMapping(value = "/security-acquisition/srl/{softwareFeatureId}", method = RequestMethod.GET)
	public @ResponseBody  List<SecurityRequirement> getSecurityRequirementsList(@PathVariable("softwareFeatureId") String softwareFeatureId) {
		
		List<Map<String,Object>> list = jdbcTemplate.queryForList("SELECT SR.ID, SR.DESCRIPTION "
				+ "FROM SECURITY_REQUIREMENT SR "
				+ "INNER JOIN SOFTWARE_FEATURE_HAS_SECURITY_REQUIREMENT SFHSR ON SFHSR.SECURITY_REQUIREMENT_ID = SR.ID "
				+ "WHERE SFHSR.SOFTWARE_FEATURE_ID = ?", softwareFeatureId);
		
        List<SecurityRequirement> srList = new ArrayList<>();        
        list.forEach(m -> {               
        	SecurityRequirement sr = new SecurityRequirement("" + (int) m.get("ID"), (String) m.get("DESCRIPTION"));
        	srList.add(sr);
        });
        
	    return srList;
	}
	
	@RequestMapping(value = "/security-acquisition/sel/{securityRequirementId}", method = RequestMethod.GET)
	public @ResponseBody  List<SecurityError> getSecurityErrorsList(@PathVariable("securityRequirementId") String securityRequirementId) {
		
		List<Map<String,Object>> list = jdbcTemplate.queryForList("SELECT SE.ID, SE.DESCRIPTION "
				+ "FROM SECURITY_ERROR SE "
				+ "INNER JOIN SECURITY_REQUIREMENT_ASSOCIATED_SECURITY_ERROR SRASE ON SRASE.SECURITY_ERROR_ID = SE.ID " 
				+ "WHERE SRASE.SECURITY_REQUIREMENT_ID = ?", securityRequirementId);
		
        List<SecurityError> seList = new ArrayList<>();        
        list.forEach(m -> {               
        	SecurityError se = new SecurityError("" + (int) m.get("ID"), (String) m.get("DESCRIPTION"));
        	seList.add(se);
        });
        
	    return seList;
	}

	private String constructHtmlDropDownList(String name, List<String> list, boolean isVisible) {
		StringBuilder htmlList = new StringBuilder();

		htmlList.append("<select name=\"").append(name).append("\" id=\"").append(name).append("\"");

		if (!isVisible) {
			htmlList.append(" hidden");
		}

		htmlList.append(">");

		for (String item : list) {
			htmlList.append("<option value=\"").append(item).append("\">").append(item).append("</option>");
		}

		htmlList.append("</select>");

		return htmlList.toString();
	}
}
