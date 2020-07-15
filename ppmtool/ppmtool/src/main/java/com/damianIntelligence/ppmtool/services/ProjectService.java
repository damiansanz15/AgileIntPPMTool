package com.damianIntelligence.ppmtool.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.damianIntelligence.ppmtool.domain.Project;
import com.damianIntelligence.ppmtool.exceptions.ProjectIdException;
import com.damianIntelligence.ppmtool.repositories.ProjectRepository;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;
	
	public Project saveOrUpdateProject(Project project) {
		try{
			project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
			return projectRepository.save(project);
		}catch(Exception e) {
			throw new ProjectIdException("Project ID "+project.getProjectIdentifier().toUpperCase()+" already exist");
		}
		
	}
	
	public Project findProjectByIdentifier(String projectId) {
		Project project=  projectRepository.findByProjectIdentifier(projectId.toUpperCase());
		if(project == null) {
			throw new ProjectIdException("Project ID "+projectId+"does not already exist");
		}
		
		return project;
	}
	
	public Iterable<Project> findAllProjects(){
		return projectRepository.findAll();
	}
	
	public void deleteProjectByIdentifier(String projectId) {
		Project project=projectRepository.findByProjectIdentifier(projectId.toUpperCase());
		if(project ==null) {
			throw new ProjectIdException("Cannot delete project with ID "+projectId);
		}
		projectRepository.delete(project);
	}
	
	
	
	
}
