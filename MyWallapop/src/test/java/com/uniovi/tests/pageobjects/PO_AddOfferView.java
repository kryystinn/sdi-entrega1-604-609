package com.uniovi.tests.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PO_AddOfferView extends PO_NavView {

	static public void fillForm(WebDriver driver, String titlep, String descriptionp, String datep, String pricep) {
		WebElement title = driver.findElement(By.name("title"));
		title.click();
		title.clear();
		title.sendKeys(titlep);
		WebElement description = driver.findElement(By.name("description"));
		description.click();
		description.clear();
		description.sendKeys(descriptionp);
		WebElement date = driver.findElement(By.name("date"));
		date.click();
		date.clear();
		date.sendKeys(datep);
		WebElement price = driver.findElement(By.name("price"));
		price.click();
		price.clear();
		price.sendKeys(pricep);
		// Pulsar el boton de Añadir.
		By boton = By.className("btn");
		driver.findElement(boton).click();
	}
}