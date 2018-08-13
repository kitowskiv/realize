package net.vincekitowski.realize.functional;

import net.vincekitowski.realize.functional.config.FunctionalTestConfig;
import net.vincentkitowski.realize.models.Post;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.text.StringContainsInOrder.stringContainsInOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@FunctionalTestConfig
@ContextConfiguration
public class PostFunctionalTests extends AbstractEventBaseFunctionalTest {

    @Test
    public void testIndexShowsRecentEvents() throws Exception {
        Post post = createAndSaveSingleEvent();
        mockMvc.perform(get("/")
                .with(user(TEST_USER_EMAIL)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(post.getTitle())))
                .andExpect(content().string(containsString(post.getFormattedStartDate())));
    }

    @Test
    public void testCanViewNewEventForm() throws Exception {
        mockMvc.perform(get("/posts/create")
                .with(user(TEST_USER_EMAIL)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Create Post")))
                .andExpect(xpath("//form[@id='eventForm']/@method").string("post"))
                .andExpect(xpath("//form[@id='eventForm']/@action").string("/posts/create"))
                .andExpect(xpath("//form//input[@name='title']").exists())
                .andExpect(xpath("//form//input[@name='startDate']").exists())
                .andExpect(xpath("//form//textarea[@name='description']").exists())
                .andExpect(xpath("//form//input[@name='location']").exists())
                .andExpect(xpath("//form//select[@name='volunteers']").exists());
    }

    @Test
    public void testCanCreateEvent() throws Exception {
        mockMvc.perform(post("/posts/create")
                .with(user(TEST_USER_EMAIL))
                .with(csrf())
                .param("title", "Test Post")
                .param("description", "This post will be great!")
                .param("startDate", "11/11/11")
                .param("location", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/posts/detail/*"));
        Post post = postRepository.findAll().get(0);
        assertNotNull(post);
    }

    @Test
    public void testCanViewEventDetails() throws Exception {
        Post post = createAndSaveSingleEvent();
        List<String> eventFields = Arrays.asList(
                post.getTitle(),
                post.getFormattedStartDate(),
                post.getLocation(),
                post.getDescription(),
                post.getVolunteersFormatted()
        );
        mockMvc.perform(get("/posts/detail/{uid}", post.getUid())
                .with(user(TEST_USER_EMAIL)))
                .andExpect(status().isOk())
                .andExpect(content().string(stringContainsInOrder(eventFields)));
    }

    @Test
    public void testDisplayErrorMessageOnInvalidEventId() throws Exception {
        mockMvc.perform(get("/posts/detail/{uid}", -1)
                .with(csrf())
                .with(user(TEST_USER_EMAIL)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("No event found with id: -1")));
    }

    @Test
    public void testCreateEventValidatesTitle() throws Exception {
        mockMvc.perform(post("/posts/create")
                .with(csrf())
                .with(user(TEST_USER_EMAIL))
                .param("title", "")
                .param("description", "Test description")
                .param("startDate", "11/11/11"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("event", "title"));
    }

    @Test
    public void testCreateEventValidatesDescription() throws Exception {
        mockMvc.perform(post("/posts/create")
                .with(csrf())
                .with(user(TEST_USER_EMAIL))
                .param("title", "Test title")
                .param("description", "")
                .param("startDate", "11/11/11"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("event", "description"));
    }

    @Test
    public void testCreateEventValidatesStartDate() throws Exception {
        mockMvc.perform(post("/posts/create")
                .with(csrf())
                .with(user(TEST_USER_EMAIL))
                .param("title", "Test Title")
                .param("description", "Test description")
                .param("startDate", ""))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("event", "startDate"));
    }

    @Test
    public void testEventDetailsPageDisplaysEditButton() throws Exception {
        Post post = createAndSaveSingleEvent();
        mockMvc.perform(get("/posts/detail/{uid}", post.getUid())
                .with(user(TEST_USER_EMAIL)))
                .andExpect(status().isOk())
                .andExpect(xpath("//a[contains(@class,'btn') and @href='/posts/update/%s']", post.getUid())
                        .string(Matchers.containsString("Edit")));
    }

    @Test
    public void testCanViewUpdateEventForm() throws Exception {
        Post post = createAndSaveSingleEvent();
        mockMvc.perform(get("/posts/update/{uid}", post.getUid())
                .with(user(TEST_USER_EMAIL)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Update Post")))
                .andExpect(xpath("//form[@method='post' and @action='/posts/update/%s']",
                        post.getUid()).exists())
                .andExpect(xpath("//form//input[@name='uid' and @value='%s']",
                        post.getUid()).exists())
                .andExpect(xpath("//form//input[@name='title' and @value='%s']",
                        post.getTitle()).exists())
                .andExpect(xpath("//form//input[@name='startDate' and @value='%s']",
                        post.getFormattedStartDate()).exists())
                .andExpect(xpath("//form//input[@name='location' and @value='%s']",
                        post.getLocation()).exists())
                .andExpect(xpath("//form//textarea[@name='description']")
                        .string(post.getDescription()));
    }

    @Test
    public void testCanUpdateEvent() throws Exception {
        Post post = createAndSaveSingleEvent();
        String newTitle = post.getTitle() + "UPDATED";
        mockMvc.perform(post("/posts/update/{uid}", post.getUid())
                .with(csrf())
                .with(user(TEST_USER_EMAIL))
                .param("uid", Integer.toString(post.getUid()))
                .param("title", newTitle)
                .param("description", post.getDescription())
                .param("startDate", post.getFormattedStartDate())
                .param("location", post.getLocation())
                .param("volunteers", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/posts/detail/"+ post.getUid()));
        Optional<Post> updatedEventRes = postRepository.findById(post.getUid());
        assertTrue(updatedEventRes.isPresent());
        Post updatedPost = updatedEventRes.get();
        assertEquals(newTitle, updatedPost.getTitle());
    }

    @Test
    public void testEventDetailPageContainsDeleteButton() throws Exception {
        Post post = createAndSaveSingleEvent();
        mockMvc.perform(get("/posts/detail/{uid}", post.getUid())
                .with(user(TEST_USER_EMAIL)))
                .andExpect(status().isOk())
                .andExpect(xpath("//form[@method='post' and @action='/posts/delete/%s']", post.getUid())
                    .exists())
                .andExpect(xpath("//form//button")
                        .string(Matchers.containsString("Delete")));
    }

    @Test
    public void testCanDeleteEvent() throws Exception {
        Post post = createAndSaveSingleEvent();
        mockMvc.perform(post("/posts/delete/{uid}", post.getUid())
                .with(user(TEST_USER_EMAIL))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/posts"));
        Optional<Post> updatedEventRes = postRepository.findById(post.getUid());
        assertFalse(updatedEventRes.isPresent());
    }


}
