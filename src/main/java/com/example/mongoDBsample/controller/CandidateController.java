package com.example.mongoDBsample.controller;

import com.example.mongoDBsample.exception.ResourceNotFoundException;
import com.example.mongoDBsample.entity.Candidate;
import com.example.mongoDBsample.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/candidate")
public class CandidateController {

    @Autowired
    private CandidateRepository candidateRepository;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Candidate add(@RequestBody Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    @GetMapping
    public List<Candidate> getAll() {
        return candidateRepository.findAll();
    }

    @GetMapping(value = "/{id}")
    public Candidate getOne(@PathVariable String id) {
        return candidateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException());
    }

    @PutMapping(value = "/{id}")
    public Candidate update(@PathVariable String id, @RequestBody Candidate updatedCandidate) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException());
        candidate.setName(updatedCandidate.getName());
        candidate.setExp(updatedCandidate.getExp());
        candidate.setEmail(updatedCandidate.getEmail());
        return candidateRepository.save(candidate);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void delete(@PathVariable String id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException());
        candidateRepository.delete(candidate);
    }

    @GetMapping("/searchByEmail")
    public Candidate searchByEmail(@RequestParam(name = "email") String email) {
        return candidateRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException());

    }

    @GetMapping("/searchByExp")
    public List<Candidate> searchByExp(@RequestParam(name = "expFrom") Double expFrom, @RequestParam(name = "expTo", required = false) Double expTo) {
        List<Candidate> result = new ArrayList<>();
        if (expTo != null) {
            result = candidateRepository.findByExpBetween(expFrom, expTo);
        } else {
            result = candidateRepository.findByExpGreaterThanEqual(expFrom);
        }
        return result;
    }
}
