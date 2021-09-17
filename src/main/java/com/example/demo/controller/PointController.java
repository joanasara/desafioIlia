package com.example.demo.controller;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.demo.dto.MessagePointDTO;
import com.example.demo.entities.PointEntities;
import com.example.demo.service.PointService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("pontos")
public class PointController {

	@Autowired
	private PointService pointService;

	
	@Transactional
	@PostMapping(value="registered")
	public ResponseEntity<MessagePointDTO> registeredPoint(@RequestHeader(value = "Hora da batida", required = true)
	@Valid @RequestBody PointEntities pointDTO,UriComponentsBuilder uriBuilder) {
		pointService.savePoint(pointDTO, uriBuilder);
		return new ResponseEntity<>(HttpStatus.OK);

	}
}
