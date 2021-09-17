package com.example.demo.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.demo.BaterPontoApplication;
import com.example.demo.dto.MessagePointDTO;
import com.example.demo.entities.PointEntities;
import com.example.demo.repository.PointRepository;
import com.example.demo.service.PointService;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = BaterPontoApplication.class)

public class ControllerTest {

	@Autowired
	private MockMvc mvc;

	@InjectMocks
	private PointService service;

	@Mock
	private PointRepository repository;

	@Test
	public void Solicitartesteformatoerrado() throws Exception {
		service = mock(PointService.class);
		String json = "{\"dataHora\":\"2022--11-30T02:57:01\"}";
		PointEntities entity = new PointEntities();
		entity.setDataHora("2022--11-30T02:57:01");
		URI uri = new URI("/batidas");
		when(service.savePoint(entity, UriComponentsBuilder.newInstance()))
				.thenReturn(ResponseEntity.status(400).body(new MessagePointDTO("Data possui formato inválido")));
		mvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();

	}

	@Test
	public void SolicitarTestePontoVazio() throws Exception {
		service = mock(PointService.class);
		String json = "{}";
		PointEntities entity = new PointEntities();
		entity.setDataHora("");
		URI uri = new URI("/batidas");
		when(service.savePoint(entity, UriComponentsBuilder.newInstance()))
				.thenReturn(ResponseEntity.status(400).body(new MessagePointDTO("Data possui formato inválido")));
		mvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();

	}

	@Test
	public void TesteCriado() throws Exception {
		String json = "{\"dataHora\":\"2022-11-30T02:57:01\"}";
		PointEntities entity = new PointEntities();
		entity.setDataHora("2022-11-30T02:57:01");
		URI uri = new URI("/batidas");
		when(service.savePoint(entity, UriComponentsBuilder.newInstance()))
				.thenReturn(ResponseEntity.created(uri).build());
		mvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
	}

	@Test
	public void DateTestduplicado() throws Exception {
		String json = "{\"dataHora\":\"2021-10-01T15:10:10\"}";
		PointEntities entity = new PointEntities();
		entity.setDataHora("2022-11-30T02:57:01");
		URI uri = new URI("/batidas");
		when(service.savePoint(entity, UriComponentsBuilder.newInstance()))
				.thenReturn(ResponseEntity.status(409).body(new MessagePointDTO("Horário já registrado")));
		mvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict()).andReturn().getResponse().getContentAsString();
	}

	@Test
	public void TesteHorAlmoco() throws Exception {
		String json = "{\"dataHora\":\"2021-10-01T16:30:10\"}";
		PointEntities entity = new PointEntities();
		entity.setDataHora("2021-10-01T16:30:10");
		URI uri = new URI("/batidas");
		when(service.savePoint(entity, UriComponentsBuilder.newInstance())).thenReturn(
				ResponseEntity.status(403).body(new MessagePointDTO("Deve haver no mínimo 1 hora de almoço")));
		mvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden()).andReturn().getResponse().getContentAsString();
	}

	@Test
	public void TesteFimSemana() throws Exception {
		service = mock(PointService.class);
		String json = "{\"dataHora\":\"2021-10-02T16:30:10\"}";
		PointEntities entity = new PointEntities();
		entity.setDataHora("2021-10-02T16:30:10");
		URI uri = new URI("/batidas");
		when(service.savePoint(entity, UriComponentsBuilder.newInstance())).thenReturn(
				ResponseEntity.status(403).body(new MessagePointDTO("Deve haver no mínimo 1 hora de almoço")));
		mvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden()).andReturn().getResponse().getContentAsString();
	}

}
