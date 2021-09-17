package com.example.demo.service;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.demo.dto.MessagePointDTO;
import com.example.demo.entities.PointEntities;
import com.example.demo.entities.PointEntities.Status;
import com.example.demo.repository.PointRepository;
import com.example.demo.util.DataUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointService {

	private final PointRepository pointRepository;

	public ResponseEntity<MessagePointDTO> savePoint(PointEntities dto, UriComponentsBuilder uriBuilder) {

		PointEntities pointEntities = new PointEntities();
		DataUtil dateUtils = new DataUtil();
		LocalDateTime date = dateUtils.convertStringIntoLocalDateTime(dto.getDataHora());
		URI uri = uriBuilder.path("/batidas/{id}").buildAndExpand(dto.getDataHora()).toUri();

		if (!dateUtils.isValidDate(dto.getDataHora())) {
			return ResponseEntity.status(400).body(new MessagePointDTO("Data e hora em formato inválido"));
		}

		if (dateUtils.weekDay(date)) {
			List<PointEntities> pointList = pointRepository.findByDateInterval(
					date.toLocalDate().atStartOfDay().toString(), date.toLocalDate().atTime(23, 59, 59).toString());

		
			if (pointList.isEmpty()) {
				pointEntities.setDataHora(dto.getDataHora());
				pointEntities.setStatus(Status.ENTRADA);
				pointRepository.save(pointEntities);
				return ResponseEntity.created(uri).build();

			} else {

				
				for (PointEntities pointEntity : pointList) {
					if (dto.getDataHora().equals(pointEntity.getDataHora())) {
						return ResponseEntity.status(409).body(new MessagePointDTO("Horário já registrado"));
					}
				}

				if (date.isBefore(LocalDateTime.parse(pointList.get(0).getDataHora()))) {
					return ResponseEntity.status(409).body(new MessagePointDTO("O momento da batida"));
				}

			
				if (pointList.get(0).getStatus() == Status.ENTRADA) {
					pointEntities.setDataHora(dto.getDataHora());
					pointEntities.setStatus(Status.SAIDA_ALMOCO);
					pointRepository.save(pointEntities);
					return ResponseEntity.created(uri).build();
				}

				if (pointList.get(0).getStatus() == Status.SAIDA_ALMOCO) {
					long diferenceInHours = Duration.between(LocalDateTime.parse(pointList.get(0).getDataHora()), date).toHours();

					if (diferenceInHours >= 1l) {
						pointEntities.setDataHora(dto.getDataHora());
						pointEntities.setStatus(Status.ENTRADA_TARDE);
						pointRepository.save(pointEntities);
						return ResponseEntity.created(uri).build();
					} else {
						return ResponseEntity.status(403).body(new MessagePointDTO("Deve haver no mínimo 1 hora de almoço"));
					}
				}

				if (pointList.get(0).getStatus() == Status.SAIDA) {
					pointEntities.setDataHora(dto.getDataHora());
					pointEntities.setStatus(Status.SAIDA);
					pointRepository.save(pointEntities);
					return ResponseEntity.created(uri).build();
				}
					

				if (pointList.get(0).getStatus() == Status.ENTRADA) {
					return ResponseEntity.status(403).body(new MessagePointDTO("Apenas 4 horários podem ser registrados"));
				}
			}
		}
		return ResponseEntity.status(403)
				.body(new MessagePointDTO("Sábado e domingo não são permitidos como dia de trabalho"));

	}
}