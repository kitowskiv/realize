package net.vincekitowski.realize.models;

import net.vincentkitowski.realize.models.Post;
import net.vincentkitowski.realize.models.Volunteer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestPost {


    @Test(expected = IllegalArgumentException.class)
    public void testCreateEventCatchesNullTitle() {
        new Post(null, "", new Date(), "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateEventCatchesEmptyTitle() {
        new Post("", "", new Date(), "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateEventCatchesNullDescription() {
        new Post("Title", null, new Date(), "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateEventCatchesBlankDescription() {
        new Post("Title", "", new Date(), "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateEventCatchesNullDate() {
        new Post("Title", "", null, "");
    }

    @Test
    public void testCanAddVolunteers() {
        Post post = new Post("Title", "A great description", new Date(), "Place");
        Volunteer vol = new Volunteer("First", "Last");
        assertEquals(0, post.getVolunteers().size());
        post.addVolunteer(vol);
        assertEquals(1, post.getVolunteers().size());
    }

    @Test
    public void canCreateEventWithVolunteers() {
        Volunteer vol1 = new Volunteer("First", "Last");
        Volunteer vol2 = new Volunteer("First", "Last");
        List<Volunteer> vols = new ArrayList<>();
        vols.add(vol1);
        vols.add(vol2);
        Post post = new Post("Title", "A great description", new Date(), "Place", vols);
        assertEquals(vols.size(), post.getVolunteers().size());
    }

}
