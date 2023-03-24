package org.uowd.sskrs.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.uowd.sskrs.models.IdentificationRequest;
import org.uowd.sskrs.models.ImplementationRequest;
import org.uowd.sskrs.models.SecurityRequirement;
import org.uowd.sskrs.models.SecurityRequirementManager;
import org.uowd.sskrs.models.SoftwareFeature;
import org.uowd.sskrs.models.SoftwareParadigm;
import org.uowd.sskrs.models.SubjectArea;
import org.uowd.sskrs.models.VerificationRequest;

@Controller
public class MainController {

	private static final String STATUS = "status";
	private static final String MESSAGE = "message";

	private static final String KNOWLEDGE_REPOSITORY_PATH = "C:\\owl\\KnowledgeGraph.owl";

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
			@ModelAttribute("request") IdentificationRequest request) {
		ModelAndView mv = new ModelAndView("security-identification");

		StringBuilder sparqlStatement;
		Query query;
		QueryExecution qe;
		ResultSet results;

		List<String> paradigms = new ArrayList<>();
		List<String> applications = new ArrayList<>();
		List<String> subjects = new ArrayList<>();

		try (InputStream in = new FileInputStream(new File(KNOWLEDGE_REPOSITORY_PATH))) {
			Model model = ModelFactory.createDefaultModel();
			model.read(in, null, "TURTLE");

			sparqlStatement = new StringBuilder();
			sparqlStatement.append(getSparqlHeader());
			sparqlStatement.append("SELECT ?x WHERE { ?x rdf:type :SoftwareParadigm }");

			query = QueryFactory.create(sparqlStatement.toString());
			qe = QueryExecutionFactory.create(query, model);
			results = qe.execSelect();

			while (results.hasNext()) {
				paradigms.add(results.next().get("?x").asResource().getLocalName());
			}

			qe.close();

			sparqlStatement = new StringBuilder();
			sparqlStatement.append(getSparqlHeader());
			sparqlStatement.append("SELECT ?x WHERE { ?x rdf:type :Application }");

			query = QueryFactory.create(sparqlStatement.toString());

			qe = QueryExecutionFactory.create(query, model);

			results = qe.execSelect();

			while (results.hasNext()) {
				applications.add(results.next().get("?x").asResource().getLocalName());
			}

			qe.close();

			sparqlStatement = new StringBuilder();
			sparqlStatement.append(getSparqlHeader());
			sparqlStatement.append("SELECT ?x WHERE { ?x rdf:type :SubjectArea }");

			query = QueryFactory.create(sparqlStatement.toString());

			qe = QueryExecutionFactory.create(query, model);

			results = qe.execSelect();

			while (results.hasNext()) {
				subjects.add(results.next().get("?x").asResource().getLocalName());
			}

			qe.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		mv.addObject("paradigms", constructHtmlDropDownList("paradigm", paradigms, true));
		mv.addObject("applications", constructHtmlDropDownList("application", applications, false));
		mv.addObject("subjects", constructHtmlDropDownList("subject", subjects, true));

		if (httpRequest.getMethod().contentEquals("POST")) {
			StringBuilder result = new StringBuilder();

			try (InputStream in = new FileInputStream(new File(KNOWLEDGE_REPOSITORY_PATH))) {
				Model model = ModelFactory.createDefaultModel();
				model.read(in, null, "TURTLE");

				sparqlStatement = new StringBuilder();
				sparqlStatement.append(getSparqlHeader());
				sparqlStatement.append("SELECT * ");
				sparqlStatement.append("WHERE { ");

				sparqlStatement.append("?SoftwareParadigm rdf:type :SoftwareParadigm .");
				sparqlStatement.append("?Application rdf:type :Application .");
				sparqlStatement.append("?SubjectArea rdf:type :SubjectArea .");

				sparqlStatement.append("?SoftwareParadigm :hasSwFeature ?SoftwareFeature .");
				sparqlStatement.append("?Application :hasSwFeature ?SoftwareFeature .");
				sparqlStatement.append("?SubjectArea :hasSwFeature ?SoftwareFeature .");
				sparqlStatement.append("?SoftwareFeature :hasSecReq ?SecurityRequirment .");
				sparqlStatement.append("?SecurityRequirment :associatedError ?SecurityError .");
				sparqlStatement.append("?SecurityError :causes ?SoftwareWeakness .");

				sparqlStatement.append(
						"FILTER regex(str(?SoftwareParadigm), str(\"^http://www.uowd.org/ontologies/2021/sscrum/softwaresecurity#")
						.append(request.getParadigm()).append("$\"), 'i') .");
				sparqlStatement.append(
						"FILTER regex(str(?Application), str(\"^http://www.uowd.org/ontologies/2021/sscrum/softwaresecurity#")
						.append(request.getApplication()).append("$\"), 'i') .");
				sparqlStatement.append(
						"FILTER regex(str(?SubjectArea), str(\"^http://www.uowd.org/ontologies/2021/sscrum/softwaresecurity#")
						.append(request.getSubject()).append("$\"), 'i')");
				sparqlStatement.append("} ");
				sparqlStatement.append("ORDER BY ASC(?SoftwareFeature) ASC(?SecurityRequirment) ");

				query = QueryFactory.create(sparqlStatement.toString());
				qe = QueryExecutionFactory.create(query, model);
				results = qe.execSelect();

				while (results.hasNext()) {
					QuerySolution q = results.next();

					result.append("<tr>");
					result.append("<td>").append(q.get("?SoftwareFeature").asResource().getLocalName()).append("</td>");
					result.append("<td>").append(q.get("?SecurityRequirment").asResource().getLocalName())
							.append("</td>");
					result.append("<td>").append(q.get("?SecurityError").asResource().getLocalName()).append("</td>");
					result.append("<td>").append(q.get("?SoftwareWeakness").asResource().getLocalName())
							.append("</td>");
					result.append("</tr>");
				}

				qe.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			mv.addObject("result", result.toString());
		}

		return mv;
	}

	@RequestMapping(value = "/s-scrum-utilities/security-implementation", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView securityImplementationView(HttpServletRequest httpRequest,
			@ModelAttribute("request") ImplementationRequest request) {
		ModelAndView mv = new ModelAndView("security-implementation");

		StringBuilder sparqlStatement;
		Query query;
		QueryExecution qe;
		ResultSet results;

		List<String> paradigms = new ArrayList<>();
		List<String> applications = new ArrayList<>();
		List<String> subjects = new ArrayList<>();
		List<String> languages = new ArrayList<>();
		List<String> technologies = new ArrayList<>();

		try (InputStream in = new FileInputStream(new File(KNOWLEDGE_REPOSITORY_PATH))) {
			Model model = ModelFactory.createDefaultModel();
			model.read(in, null, "TURTLE");

			sparqlStatement = new StringBuilder();
			sparqlStatement.append(getSparqlHeader());
			sparqlStatement.append("SELECT ?x WHERE { ?x rdf:type :SoftwareParadigm }");

			query = QueryFactory.create(sparqlStatement.toString());
			qe = QueryExecutionFactory.create(query, model);
			results = qe.execSelect();

			while (results.hasNext()) {
				paradigms.add(results.next().get("?x").asResource().getLocalName());
			}

			qe.close();

			sparqlStatement = new StringBuilder();
			sparqlStatement.append(getSparqlHeader());
			sparqlStatement.append("SELECT ?x WHERE { ?x rdf:type :Application }");

			query = QueryFactory.create(sparqlStatement.toString());

			qe = QueryExecutionFactory.create(query, model);

			results = qe.execSelect();

			while (results.hasNext()) {
				applications.add(results.next().get("?x").asResource().getLocalName());
			}

			qe.close();

			sparqlStatement = new StringBuilder();
			sparqlStatement.append(getSparqlHeader());
			sparqlStatement.append("SELECT ?x WHERE { ?x rdf:type :SubjectArea }");

			query = QueryFactory.create(sparqlStatement.toString());

			qe = QueryExecutionFactory.create(query, model);

			results = qe.execSelect();

			while (results.hasNext()) {
				subjects.add(results.next().get("?x").asResource().getLocalName());
			}

			qe.close();

			sparqlStatement = new StringBuilder();
			sparqlStatement.append(getSparqlHeader());
			sparqlStatement.append("SELECT ?x WHERE { ?x rdf:type :Language }");

			query = QueryFactory.create(sparqlStatement.toString());

			qe = QueryExecutionFactory.create(query, model);

			results = qe.execSelect();

			while (results.hasNext()) {
				languages.add(results.next().get("?x").asResource().getLocalName());
			}

			qe.close();

			sparqlStatement = new StringBuilder();
			sparqlStatement.append(getSparqlHeader());
			sparqlStatement.append("SELECT ?x WHERE { ?x rdf:type :Technology }");

			query = QueryFactory.create(sparqlStatement.toString());

			qe = QueryExecutionFactory.create(query, model);

			results = qe.execSelect();

			while (results.hasNext()) {
				technologies.add(results.next().get("?x").asResource().getLocalName());
			}

			qe.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		mv.addObject("paradigms", constructHtmlDropDownList("paradigm", paradigms, true));
		mv.addObject("applications", constructHtmlDropDownList("application", applications, false));
		mv.addObject("subjects", constructHtmlDropDownList("subject", subjects, true));
		mv.addObject("languages", constructHtmlDropDownList("language", languages, true));
		mv.addObject("technologies", constructHtmlDropDownList("technology", technologies, true));

		if (httpRequest.getMethod().contentEquals("POST")) {
			StringBuilder result = new StringBuilder();

			try (InputStream in = new FileInputStream(new File(KNOWLEDGE_REPOSITORY_PATH))) {
				Model model = ModelFactory.createDefaultModel();
				model.read(in, null, "TURTLE");

				sparqlStatement = new StringBuilder();
				sparqlStatement.append(getSparqlHeader());
				sparqlStatement.append("SELECT * ");
				sparqlStatement.append("WHERE { ");

				sparqlStatement.append("?SoftwareParadigm rdf:type :SoftwareParadigm .");
				sparqlStatement.append("?Application rdf:type :Application .");
				sparqlStatement.append("?SubjectArea rdf:type :SubjectArea .");

				sparqlStatement.append("?SoftwareParadigm :hasSwFeature ?SoftwareFeature .");
				sparqlStatement.append("?Application :hasSwFeature ?SoftwareFeature .");
				sparqlStatement.append("?SubjectArea :hasSwFeature ?SoftwareFeature .");

				sparqlStatement.append("?SoftwareFeature :hasSecReq ?SecurityRequirment .");
				sparqlStatement.append("?SecurityRequirment :followedbyCP ?ConstructionPractice .");
				sparqlStatement.append("?ConstructionPractice :relatedLanguage ?Language .");
				sparqlStatement.append("?Language :provides ?Technology .");

				sparqlStatement.append("?SecurityRequirment :associatedError ?SecurityError .");
				sparqlStatement.append("?SecurityError :causes ?SoftwareWeakness .");

				sparqlStatement.append("?ConstructionPractice :resource ?r .");
				sparqlStatement.append("BIND (STR(?r) AS ?str_r) .");

				sparqlStatement.append(
						"FILTER regex(str(?SoftwareParadigm), str(\"^http://www.uowd.org/ontologies/2021/sscrum/softwaresecurity#")
						.append(request.getParadigm()).append("$\"), 'i') .");
				sparqlStatement.append(
						"FILTER regex(str(?Application), str(\"^http://www.uowd.org/ontologies/2021/sscrum/softwaresecurity#")
						.append(request.getApplication()).append("$\"), 'i') .");
				sparqlStatement.append(
						"FILTER regex(str(?SubjectArea), str(\"^http://www.uowd.org/ontologies/2021/sscrum/softwaresecurity#")
						.append(request.getSubject()).append("$\"), 'i') .");
				sparqlStatement.append(
						"FILTER regex(str(?Language), str(\"^http://www.uowd.org/ontologies/2021/sscrum/softwaresecurity#")
						.append(request.getLanguage()).append("$\"), 'i') .");
				sparqlStatement.append(
						"FILTER regex(str(?Technology), str(\"^http://www.uowd.org/ontologies/2021/sscrum/softwaresecurity#")
						.append(request.getTechnology()).append("$\"), 'i')");

				sparqlStatement.append("} ");
				sparqlStatement.append("ORDER BY ASC(?SoftwareFeature) ASC(?SecurityRequirment) ");

				query = QueryFactory.create(sparqlStatement.toString());
				qe = QueryExecutionFactory.create(query, model);
				results = qe.execSelect();

				while (results.hasNext()) {
					QuerySolution q = results.next();

					result.append("<tr>");
					result.append("<td>").append(q.get("?SoftwareFeature").asResource().getLocalName()).append("</td>");
					result.append("<td>").append(q.get("?SecurityRequirment").asResource().getLocalName())
							.append("</td>");
					result.append("<td>").append(q.get("?SecurityError").asResource().getLocalName()).append("</td>");
					result.append("<td>").append(q.get("?SoftwareWeakness").asResource().getLocalName())
							.append("</td>");
					result.append("<td>").append(q.get("?ConstructionPractice").asResource().getLocalName())
							.append("</td>");
					result.append("<td><a target=\"_blank\" href=\"").append(q.get("?str_r"))
							.append("\">View</a></td>");
					result.append("</tr>");
				}

				qe.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			mv.addObject("result", result.toString());
		}

		return mv;
	}

	@RequestMapping(value = "/s-scrum-utilities/security-verification", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView securityVerificationView(HttpServletRequest httpRequest,
			@ModelAttribute("request") VerificationRequest request) {
		ModelAndView mv = new ModelAndView("security-verification");

		StringBuilder sparqlStatement;
		Query query;
		QueryExecution qe;
		ResultSet results;

		List<String> paradigms = new ArrayList<>();
		List<String> applications = new ArrayList<>();
		List<String> subjects = new ArrayList<>();
		List<String> languages = new ArrayList<>();
		List<String> technologies = new ArrayList<>();

		try (InputStream in = new FileInputStream(new File(KNOWLEDGE_REPOSITORY_PATH))) {
			Model model = ModelFactory.createDefaultModel();
			model.read(in, null, "TURTLE");

			sparqlStatement = new StringBuilder();
			sparqlStatement.append(getSparqlHeader());
			sparqlStatement.append("SELECT ?x WHERE { ?x rdf:type :SoftwareParadigm }");

			query = QueryFactory.create(sparqlStatement.toString());
			qe = QueryExecutionFactory.create(query, model);
			results = qe.execSelect();

			while (results.hasNext()) {
				paradigms.add(results.next().get("?x").asResource().getLocalName());
			}

			qe.close();

			sparqlStatement = new StringBuilder();
			sparqlStatement.append(getSparqlHeader());
			sparqlStatement.append("SELECT ?x WHERE { ?x rdf:type :Application }");

			query = QueryFactory.create(sparqlStatement.toString());

			qe = QueryExecutionFactory.create(query, model);

			results = qe.execSelect();

			while (results.hasNext()) {
				applications.add(results.next().get("?x").asResource().getLocalName());
			}

			qe.close();

			sparqlStatement = new StringBuilder();
			sparqlStatement.append(getSparqlHeader());
			sparqlStatement.append("SELECT ?x WHERE { ?x rdf:type :SubjectArea }");

			query = QueryFactory.create(sparqlStatement.toString());

			qe = QueryExecutionFactory.create(query, model);

			results = qe.execSelect();

			while (results.hasNext()) {
				subjects.add(results.next().get("?x").asResource().getLocalName());
			}

			qe.close();

			sparqlStatement = new StringBuilder();
			sparqlStatement.append(getSparqlHeader());
			sparqlStatement.append("SELECT ?x WHERE { ?x rdf:type :Language }");

			query = QueryFactory.create(sparqlStatement.toString());

			qe = QueryExecutionFactory.create(query, model);

			results = qe.execSelect();

			while (results.hasNext()) {
				languages.add(results.next().get("?x").asResource().getLocalName());
			}

			qe.close();

			sparqlStatement = new StringBuilder();
			sparqlStatement.append(getSparqlHeader());
			sparqlStatement.append("SELECT ?x WHERE { ?x rdf:type :Technology }");

			query = QueryFactory.create(sparqlStatement.toString());

			qe = QueryExecutionFactory.create(query, model);

			results = qe.execSelect();

			while (results.hasNext()) {
				technologies.add(results.next().get("?x").asResource().getLocalName());
			}

			qe.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		mv.addObject("paradigms", constructHtmlDropDownList("paradigm", paradigms, true));
		mv.addObject("applications", constructHtmlDropDownList("application", applications, false));
		mv.addObject("subjects", constructHtmlDropDownList("subject", subjects, true));
		mv.addObject("languages", constructHtmlDropDownList("language", languages, true));
		mv.addObject("technologies", constructHtmlDropDownList("technology", technologies, true));

		if (httpRequest.getMethod().contentEquals("POST")) {
			StringBuilder result = new StringBuilder();

			try (InputStream in = new FileInputStream(new File(KNOWLEDGE_REPOSITORY_PATH))) {
				Model model = ModelFactory.createDefaultModel();
				model.read(in, null, "TURTLE");

				sparqlStatement = new StringBuilder();
				sparqlStatement.append(getSparqlHeader());
				sparqlStatement.append("SELECT * ");
				sparqlStatement.append("WHERE { ");

				sparqlStatement.append("?SoftwareParadigm rdf:type :SoftwareParadigm .");
				sparqlStatement.append("?Application rdf:type :Application .");
				sparqlStatement.append("?SubjectArea rdf:type :SubjectArea .");

				sparqlStatement.append("?SoftwareParadigm :hasSwFeature ?SoftwareFeature .");
				sparqlStatement.append("?Application :hasSwFeature ?SoftwareFeature .");
				sparqlStatement.append("?SubjectArea :hasSwFeature ?SoftwareFeature .");

				sparqlStatement.append("?SoftwareFeature :hasSecReq ?SecurityRequirment .");
				sparqlStatement.append("?SecurityRequirment :followedbyVP ?VerificationPractice .");
				sparqlStatement.append("?SecurityRequirment :associatedError ?SecurityError .");
				sparqlStatement.append("?SecurityError :causes ?SoftwareWeakness .");

				sparqlStatement.append("?SecurityError :relatedLanguage ?Language .");
				sparqlStatement.append("?Language :provides ?Technology .");

				sparqlStatement.append(
						"FILTER regex(str(?SoftwareParadigm), str(\"^http://www.uowd.org/ontologies/2021/sscrum/softwaresecurity#")
						.append(request.getParadigm()).append("$\"), 'i') .");
				sparqlStatement.append(
						"FILTER regex(str(?Application), str(\"^http://www.uowd.org/ontologies/2021/sscrum/softwaresecurity#")
						.append(request.getApplication()).append("$\"), 'i') .");
				sparqlStatement.append(
						"FILTER regex(str(?SubjectArea), str(\"^http://www.uowd.org/ontologies/2021/sscrum/softwaresecurity#")
						.append(request.getSubject()).append("$\"), 'i') .");
				sparqlStatement.append(
						"FILTER regex(str(?Language), str(\"^http://www.uowd.org/ontologies/2021/sscrum/softwaresecurity#")
						.append(request.getLanguage()).append("$\"), 'i') .");
				sparqlStatement.append(
						"FILTER regex(str(?Technology), str(\"^http://www.uowd.org/ontologies/2021/sscrum/softwaresecurity#")
						.append(request.getTechnology()).append("$\"), 'i')");

				sparqlStatement.append("} ");
				sparqlStatement.append("ORDER BY ASC(?SoftwareFeature) ASC(?SecurityRequirment) ");

				query = QueryFactory.create(sparqlStatement.toString());
				qe = QueryExecutionFactory.create(query, model);
				results = qe.execSelect();

				while (results.hasNext()) {
					QuerySolution q = results.next();

					result.append("<tr>");
					result.append("<td>").append(q.get("?SoftwareFeature").asResource().getLocalName()).append("</td>");
					result.append("<td>").append(q.get("?SecurityRequirment").asResource().getLocalName())
							.append("</td>");
					result.append("<td>").append(q.get("?SecurityError").asResource().getLocalName()).append("</td>");
					result.append("<td>").append(q.get("?SoftwareWeakness").asResource().getLocalName())
							.append("</td>");
					result.append("<td>").append(q.get("?VerificationPractice").asResource().getLocalName())
							.append("</td>");
					result.append("</tr>");
				}

				qe.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			mv.addObject("result", result.toString());
		}

		return mv;
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
	    							"Error occurred while adding Software Paradigm '" + securityRequirementManager.getSecurityRequirementDescription() + "'.");
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
		
		mnv.addObject("securityRequirementManager", new SecurityRequirementManager("" + sf.getId(), sf.getDescription(), "" + sp.getId(), sp.getDescription(), "" + sa.getId(), sa.getDescription(), "-1", "", "N"));
		
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
	public ModelAndView manageSecurityRequirementSecurityErrorView(@ModelAttribute("securityRequirement") SecurityRequirement securityRequirement) {
		ModelAndView mnv = new ModelAndView("mse-manage");
		
		
		
		SecurityRequirement sr = jdbcTemplate.queryForObject("SELECT [ID],[DESCRIPTION] FROM [SSKMS].[dbo].[SECURITY_REQUIREMENT] WHERE ID = ?", (rs, rowNum) -> new SecurityRequirement("" + rs.getInt("ID"), rs.getString("DESCRIPTION")), securityRequirement.getId());
		mnv.addObject("securityRequirement", sr);
		
//		List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT SR.ID AS 'SECURITY REQUIREMENT ID', SR.DESCRIPTION AS 'SECURITY REQUIREMENT DESCRIPTION' "
//		+ "FROM [SSKMS].[dbo].[SOFTWARE_FEATURE_HAS_SECURITY_REQUIREMENT] AS SFHSR "
//		+ "INNER JOIN SOFTWARE_FEATURE SF ON SF.ID = SFHSR.SOFTWARE_FEATURE_ID "
//		+ "INNER JOIN SECURITY_REQUIREMENT SR ON SR.ID = SFHSR.SECURITY_REQUIREMENT_ID "
//		+ "WHERE SFHSR.SOFTWARE_FEATURE_ID = ?", softwareFeature.getId());
//		
//		List<SecurityRequirement> srList = new ArrayList<>();
//		
//		list.forEach(m -> {
//			SecurityRequirement sr = new SecurityRequirement((int) m.get("SECURITY REQUIREMENT ID"), (String) m.get("SECURITY REQUIREMENT DESCRIPTION")); 
//			srList.add(sr);
//		});
//		
//		mnv.addObject("result", srList);
		
		return mnv;
	}

	@GetMapping(path = "/security-acquisition/mcp")
	public ModelAndView manageContructionPracticeView() {
		return new ModelAndView("mcp");
	}

	@GetMapping(path = "/security-acquisition/mvp")
	public ModelAndView manageVerificationPracticeView() {
		return new ModelAndView("mvp");
	}

	private static String getSparqlHeader() {
		StringBuilder sparqlHeader = new StringBuilder();
		sparqlHeader.append("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ");
		sparqlHeader.append("PREFIX owl: <http://www.w3.org/2002/07/owl#> ");
		sparqlHeader.append("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> ");
		sparqlHeader.append("PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> ");
		sparqlHeader.append("PREFIX : <http://www.uowd.org/ontologies/2021/sscrum/softwaresecurity#> ");

		return sparqlHeader.toString();
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
