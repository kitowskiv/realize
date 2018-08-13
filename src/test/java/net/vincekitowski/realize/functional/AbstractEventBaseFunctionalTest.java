package net.vincekitowski.realize.functional;

import net.vincentkitowski.realize.models.Post;
import net.vincentkitowski.realize.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class AbstractEventBaseFunctionalTest extends AbstractBaseFunctionalTest {

    @Autowired
    PostRepository postRepository;

    protected Post createAndSaveSingleEvent() {
        Date eventDate = new Date();
        Post newPost = new Post("The Title", "The Description", eventDate, "The Location");
        postRepository.save(newPost);
        return newPost;
    }

    protected List<Post> createAndSaveMultipleEvents(int numberOfEvents) {
        List<Post> posts = new ArrayList<>();
        for (int i=0; i<numberOfEvents; i++) {
            Post e = new Post("Post "+Integer.toString(i), "The Description", new Date(), "");
            postRepository.save(e);
            posts.add(e);
        }
        return posts;
    }

}
