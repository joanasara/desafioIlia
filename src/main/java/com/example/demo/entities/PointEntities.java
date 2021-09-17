package com.example.demo.entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "Ponto")
public class PointEntities implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotEmpty(message = "Obrigatório Campo")
	private String dataHora;

	private Status status;

	public enum Status {
		ENTRADA, SAIDA_ALMOCO, ENTRADA_TARDE, SAIDA
	}

	public String getDataHora() {
		return dataHora;
	}

	public boolean validateDate(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
		format.setLenient(false);
		try {
			format.parse(date);
			return true;
		} catch (Exception e) {
			return false;

		}
	}

	
	

}
