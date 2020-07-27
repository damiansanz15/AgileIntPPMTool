package com.damianIntelligence.ppmtool.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.damianIntelligence.ppmtool.domain.Backlog;
import com.damianIntelligence.ppmtool.domain.Project;
import com.damianIntelligence.ppmtool.domain.User;
import com.damianIntelligence.ppmtool.exceptions.ProjectIdException;
import com.damianIntelligence.ppmtool.exceptions.ProjectNotFoundException;
import com.damianIntelligence.ppmtool.repositories.BacklogRepository;
import com.damianIntelligence.ppmtool.repositories.ProjectRepository;
import com.damianIntelligence.ppmtool.repositories.UserRepository;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private BacklogRepository backlogRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public Project saveOrUpdateProject(Project project, String username) {
		//project.getId==null
		
		//find by db id  -> return null;;
		
		if(project.getId()!=null) {
			Project existingProject=projectRepository.findByProjectIdentifier(project.getProjectIdentifier());
			
			if(existingProject!=null && (!existingProject.getProjectLeader().equals(username))) {
				throw new ProjectNotFoundException("Project not found in your account");
			}else if(existingProject==null) {
				throw new ProjectNotFoundException("Project with ID: "+project.getProjectIdentifier()+" cannot be updated because it does not exist");
			}
			
			
			
		}
		
		
		
		try{
			
			
			
			User user=userRepository.findByUsername(username);
			project.setUser(user);
			project.setProjectLeader(user.getUsername());
			
			project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
			
			if(project.getId()==null) {
				Backlog backlog=new Backlog();
				project.setBacklog(backlog);
				backlog.setProject(project);
				backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
			}
			
			if(project.getId()!=null) {
				project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
			}
			
			return projectRepository.save(project);
		}catch(Exception e) {
			throw new ProjectIdException("Project ID "+project.getProjectIdentifier().toUpperCase()+" already exist");
		}
		
	}
	
	public Project findProjectByIdentifier(String projectId, String username) {
		Project project=  projectRepository.findByProjectIdentifier(projectId.toUpperCase());
		if(project == null) {
			throw new ProjectIdException("Project ID "+projectId+"does not already exist");
		}
		
		if(!project.getProjectLeader().equals(username)) {
			throw new ProjectNotFoundException("Project not foun in your account");
		}
		
		return project;
	}
	
	public Iterable<Project> findAllProjects(String username){
		return projectRepository.findAllByProjectLeader(username);
	}
	
	public void deleteProjectByIdentifier(String projectId,String username) {
		/*
		 * Project
		 * project=projectRepository.findByProjectIdentifier(projectId.toUpperCase());
		 * if(project ==null) { throw new
		 * ProjectIdException("Cannot delete project with ID "+projectId); }
		 */
		
		
		
		projectRepository.delete(findProjectByIdentifier(projectId, username));
	}
	
	
	
	
}
