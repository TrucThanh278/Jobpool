package com.ntt.JobPool.controller;

import com.ntt.JobPool.domain.Skill;
import com.ntt.JobPool.domain.response.ResultPaginationDTO;
import com.ntt.JobPool.service.SkillService;
import com.ntt.JobPool.utils.annotations.ApiMessage;
import com.ntt.JobPool.utils.exception.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class SkillController {

  @Autowired
  private SkillService skillService;

  @PostMapping("/skills")
  @ApiMessage("Create a new skill")
  public ResponseEntity<Skill> createSkill(@Valid @RequestBody Skill s) throws IdInvalidException {
    if (s.getName() != null && this.skillService.isNameExist(s.getName())) {
      throw new IdInvalidException("Kĩ năng " + s.getName() + " đã tồn tại !");
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(this.skillService.createSkill(s));
  }

  @PutMapping("/skills")
  @ApiMessage("Update a skill")
  public ResponseEntity<Skill> updateSkill(@Valid @RequestBody Skill s) throws IdInvalidException {
    Skill currentSkill = this.skillService.getSkillById(s.getId());
    if (currentSkill == null) {
      throw new IdInvalidException("Kĩ năng với id = " + s.getId() + " không tồn tại !");
    }

    if (s.getName() != null && this.skillService.isNameExist(s.getName())) {
      throw new IdInvalidException("Kĩ năng có tên " + s.getName() + " đã tồn tại !");
    }

    currentSkill.setName(s.getName());
    return ResponseEntity.ok().body(this.skillService.updateSkill(currentSkill));
  }

  @GetMapping("/skills")
  @ApiMessage("get all skills")
  public ResponseEntity<ResultPaginationDTO> getAllSkills(@Filter Specification<Skill> spec,
      Pageable pageable) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(this.skillService.getAllSkills(spec, pageable));
  }

  @DeleteMapping("/skills/{id}")
  @ApiMessage("Delete a skill")
  public ResponseEntity<Void> deleteSkill(@PathVariable("id") long id) throws IdInvalidException {
    Skill currentSkill = this.skillService.getSkillById(id);

    if (currentSkill == null) {
      throw new IdInvalidException("Skill id = " + id + " khong hop le !");
    }

    this.skillService.deleteSkill(id);
    return ResponseEntity.ok().body(null);
  }
}
