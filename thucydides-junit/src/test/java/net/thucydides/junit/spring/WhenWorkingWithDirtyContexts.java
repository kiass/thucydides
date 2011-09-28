package net.thucydides.junit.spring;

import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.ManagedPages;
import net.thucydides.core.pages.Pages;
import net.thucydides.junit.runners.ThucydidesRunner;
import net.thucydides.junit.spring.SpringIntegration;
import net.thucydides.junit.spring.samples.GizmoService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Resource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

@RunWith(ThucydidesRunner.class)
@ContextConfiguration(locations = "/spring/config.xml")
public class WhenWorkingWithDirtyContexts {

    @Managed
    WebDriver driver;

    @ManagedPages(defaultUrl = "http://www.google.com")
    public Pages pages;

    @Rule
    public SpringIntegration springIntegration = SpringIntegration.forClass(this.getClass());

    @Autowired
    public GizmoService gizmoService;

    @Test
    @DirtiesContext
    public void shouldNotBeAffectedByTheOtherTest() {
        assertThat(gizmoService.getName(), is("Gizmos"));
        gizmoService.setName("New Gizmos");
    }

    @Test
    @DirtiesContext
    public void shouldNotBeAffectedByTheOtherTestEither() {
        assertThat(gizmoService.getName(), is("Gizmos"));
        gizmoService.setName("New Gizmos");
    }

}