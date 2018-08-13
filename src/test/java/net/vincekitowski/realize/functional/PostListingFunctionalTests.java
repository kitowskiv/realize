package net.vincekitowski.realize.functional;

import net.vincekitowski.realize.functional.config.FunctionalTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;


@RunWith(SpringRunner.class)
@FunctionalTestConfig
@ContextConfiguration
public class PostListingFunctionalTests extends AbstractEventBaseFunctionalTest {

    @Test
    public void testEventTitlesLinkToEventDetailsPages() throws Exception {
        int numberOfEvents = 5;
        createAndSaveMultipleEvents(numberOfEvents);
        mockMvc.perform(get("/")
                .with(user(TEST_USER_EMAIL)))
                .andExpect(xpath("//tbody//tr//a[starts-with(@href,'/posts/detail/')]")
                        .nodeCount(numberOfEvents));
    }

    @Test
    public void testCanViewAddEventButtonOnVolunteerListing() throws Exception {
        mockMvc.perform(get("/posts")
                .with(user(TEST_USER_EMAIL)))
                .andExpect(status().isOk())
                .andExpect(xpath("//a[@href='/posts/create']").string(containsString("Create Post")));
    }

}
