package com.uniovi.tests.pageobjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.uniovi.tests.util.SeleniumUtils;

public class PO_PrivateView extends PO_NavView {

//	static public void fillFormAddMark(WebDriver driver, int userOrder, String titlep) {
//		// Esperamos 5 segundo a que carge el DOM porque en algunos equipos falla
//		SeleniumUtils.esperarSegundos(driver, 5);
//		// Seleccionamos el usuarios userOrder
//		new Select(driver.findElement(By.id("user"))).selectByIndex(userOrder);
//		// Rellenemos el campo de descripción
//		WebElement description = driver.findElement(By.name("title"));
//		description.clear();
//		description.sendKeys(titlep);
//		By boton = By.className("btn");
//		driver.findElement(boton).click();
//	}
	
	public static void logout(WebDriver driver, String btn) {
		//clickamos la opción principal.username.
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "btnGroup", getTimeout());
		elementos.get(0).click();
		//Esperamos a que aparezca el menú de opciones.
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "usersdropdownMenuButton", getTimeout());
		//Clickamos la opción de logout
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", btn, getTimeout());
		elementos.get(0).click();
	}
}