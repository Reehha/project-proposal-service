package com.example.projectproposal.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.projectproposal.dto.ProjectRequest;
import com.example.projectproposal.model.ProjectProposal;
import com.example.projectproposal.service.ProjectService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/projects")
@Validated
public class ProjectController {

	private final ProjectService projects;

	public ProjectController(ProjectService projects) {
		this.projects = projects;
	}

	@PostMapping
	public ResponseEntity<ProjectProposal> create(@Valid @RequestBody ProjectRequest req, Authentication auth) {
		String uid = (String) auth.getPrincipal();
		var created = projects.create(req, uid);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@PutMapping("/{pid}")
	public ResponseEntity<ProjectProposal> update(@PathVariable String pid, @Valid @RequestBody ProjectRequest req,
			Authentication auth) {
		String uid = (String) auth.getPrincipal();
		return ResponseEntity.ok(projects.update(pid, req, uid));
	}

	@GetMapping("/{pid}")
	public ResponseEntity<ProjectProposal> get(@PathVariable String pid, Authentication auth) {
		String uid = (String) auth.getPrincipal();
		return ResponseEntity.ok(projects.get(pid, uid));
	}

//✅ ADD THIS ENDPOINT
	@GetMapping
	public ResponseEntity<java.util.List<ProjectProposal>> getAll() {
		return ResponseEntity.ok(projects.getAll());
	}

}
