package net.thucydides.jbehave.internals;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import net.thucydides.core.ThucydidesListeners;
import net.thucydides.core.ThucydidesReports;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.reports.ReportService;
import net.thucydides.core.steps.ExecutedStepDescription;
import net.thucydides.core.steps.StepEventBus;
import net.thucydides.core.steps.StepFailure;
import net.thucydides.core.util.NameConverter;
import net.thucydides.core.webdriver.Configuration;
import org.codehaus.plexus.util.StringUtils;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.model.GivenStories;
import org.jbehave.core.model.Meta;
import org.jbehave.core.model.Narrative;
import org.jbehave.core.model.OutcomesTable;
import org.jbehave.core.model.Scenario;
import org.jbehave.core.model.Story;
import org.jbehave.core.model.StoryDuration;
import org.jbehave.core.reporters.StoryReporter;

import java.util.List;
import java.util.Map;

/**
 * A description goes here.
 * User: johnsmart
 * Date: 15/05/12
 * Time: 5:38 PM
 */
public class ThucydidesReporter implements StoryReporter {

    private ThucydidesListeners thucydidesListeners;
    private ReportService reportService;
    private final Configuration systemConfiguration;

    public ThucydidesReporter(Configuration systemConfiguration) {
        this.systemConfiguration = systemConfiguration;
    }

    public void storyNotAllowed(Story story, String s) {
    }

    public void storyCancelled(Story story, StoryDuration storyDuration) {
    }

    public void beforeStory(Story story, boolean b) {
        System.out.println("Before story: " + story.getDescription());

        String storyName = removeSuffixFrom(story.getName());
        String storyTitle = NameConverter.humanize(storyName);
        reportService  = ThucydidesReports.getReportService(systemConfiguration);
        thucydidesListeners = ThucydidesReports.setupListeners(systemConfiguration);
        StepEventBus.getEventBus().testSuiteStarted(net.thucydides.core.model.Story.withId(storyName, storyTitle));
        registerStoryIssues(story.getMeta());
    }

    private List<String> getIssueOrIssuesPropertyValues(Meta metaData) {
        String issue = metaData.getProperty("issue");
        String issues = metaData.getProperty("issues");
        String allIssues = Joiner.on(',').skipNulls().join(issue, issues);

        return Lists.newArrayList(Splitter.on(',').omitEmptyStrings().trimResults().split(allIssues));


    }

    private void registerIssues(Meta metaData) {
        List<String> issues = getIssueOrIssuesPropertyValues(metaData);

        if (!issues.isEmpty()) {
            StepEventBus.getEventBus().addIssuesToCurrentTest(issues);
        }
    }

    private void registerStoryIssues(Meta metaData) {
        List<String> issues = getIssueOrIssuesPropertyValues(metaData);

        if (!issues.isEmpty()) {
            StepEventBus.getEventBus().addIssuesToCurrentStory(issues);
        }
    }

    private String removeSuffixFrom(String name) {
        return (name.contains(".")) ? name.substring(0, name.indexOf(".")) :  name;
    }

    public void afterStory(boolean b) {
        System.out.println("After story");
        StepEventBus.getEventBus().testSuiteFinished();
        generateReportsFor(thucydidesListeners.getResults());
    }

    private void generateReportsFor(final List<TestOutcome> testRunResults) {
        reportService.generateReportsFor(testRunResults);
    }

    public void narrative(Narrative narrative) {
        System.out.println("NARRATIVE: " + narrative);
    }

    public void scenarioNotAllowed(Scenario scenario, String s) {
    }

    public void beforeScenario(String scenarioTitle) {
        System.out.println("BEFORE SCENARIO: " + scenarioTitle);
        StepEventBus.getEventBus().testStarted(scenarioTitle);
    }

    public void scenarioMeta(Meta meta) {
        System.out.println("SCENARIO META: " + meta);
        registerIssues(meta);
    }

    public void afterScenario() {
        System.out.println("AFTER SCENARIO");
        StepEventBus.getEventBus().testFinished();
    }

    public void givenStories(GivenStories givenStories) {
        System.out.println("GIVEN STORIES: " + givenStories);
    }

    public void givenStories(List<String> strings) {
        System.out.println("GIVEN STORIES: " + strings);
    }

    public void beforeExamples(List<String> strings, ExamplesTable examplesTable) {
        System.out.println("BEFORE EXAMPLES: " + strings);
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void example(Map<String, String> stringStringMap) {
        System.out.println("EXAMPLE: " + stringStringMap);
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void afterExamples() {
        System.out.println("AFTER EXAMPLES");
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void beforeStep(String stepTitle) {
        System.out.println("BEFORE STEP: " + stepTitle);
        StepEventBus.getEventBus().stepStarted(ExecutedStepDescription.withTitle(stepTitle));
    }

    public void successful(String title) {
        System.out.println("STEP SUCCESSFUL " + title);
        StepEventBus.getEventBus().updateCurrentStepTitle(title);
        StepEventBus.getEventBus().stepFinished();
    }

    public void ignorable(String title) {
        System.out.println("STEP IGNORED " + title);
        StepEventBus.getEventBus().updateCurrentStepTitle(title);
        StepEventBus.getEventBus().stepIgnored();
    }

    public void pending(String stepTitle) {
        System.out.println("STEP PENDING " + stepTitle);
        StepEventBus.getEventBus().stepStarted(ExecutedStepDescription.withTitle(stepTitle));
        StepEventBus.getEventBus().stepPending();
    }

    public void notPerformed(String stepTitle) {
        System.out.println("STEP SKIPPED " + stepTitle);
        StepEventBus.getEventBus().stepStarted(ExecutedStepDescription.withTitle(stepTitle));
        StepEventBus.getEventBus().stepIgnored();
    }

    public void failed(String stepTitle, Throwable cause) {
        System.out.println("STEP FAILED " + stepTitle);
        StepEventBus.getEventBus().updateCurrentStepTitle(stepTitle);
        StepEventBus.getEventBus().stepFailed(new StepFailure(ExecutedStepDescription.withTitle(stepTitle), cause));
    }

    public void failedOutcomes(String s, OutcomesTable outcomesTable) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void restarted(String s, Throwable throwable) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void dryRun() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void pendingMethods(List<String> strings) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
