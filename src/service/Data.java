package service;

import entite.Photo;
import entite.Person;
import entite.Note;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Data {
    private List<Photo> photos;
    private List<Person> users;
    private final String noteFileName = "note.txt";


    public Data() {
        photos = new ArrayList<>();
        users = new ArrayList<>();

        photos.add(new Photo("Paris", "parisphoto.jpg"));
        photos.add(new Photo("Bruxelles", "bruxellesphoto.jpeg"));

        users.add(new Person("toto", "toto123"));
        users.add(new Person("admin", "admin123"));
    }

    public List<String> getCityNames() {
        List<String> cityNames = new ArrayList<>();
        for (Photo photo : photos) {
            cityNames.add(photo.getName());
        }
        return cityNames;
    }

    public String getUrlByCityName(String cityName) {
        for (Photo photo : photos) {
            if (photo.getName().equalsIgnoreCase(cityName)) {
                return photo.getUrl();
            }
        }
        return null;
    }
    
    public Person authenticateUser(String username, String password) throws Exception {
        for (Person user : users) {
            if (user.getName().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        throw new Exception("Identifiants incorrects");
    }
    
    public void saveNote(Note note) throws IOException {
        List<Note> notes = getAllNotes();
        notes.removeIf(n -> n.getPersonne().getName().equals(note.getPersonne().getName()) && n.getPhoto().getName().equals(note.getPhoto().getName()));
        notes.add(note);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(noteFileName))) {
            for (Note n : notes) {
                writer.write(n.getPersonne().getName() + ";" + n.getPhoto().getName() + ";" + n.getNote());
                writer.newLine();
            }
        }
    }
    
    public Note getNoteForUserAndPhoto(String userName, String photoName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(noteFileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts[0].equals(userName) && parts[1].equals(photoName)) {
                    Photo photo = new Photo(parts[1], "");
                    Person user = new Person(parts[0], "");
                    int noteValue = Integer.parseInt(parts[2]);
                    return new Note(noteValue, photo, user);
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }


    private List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(noteFileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                notes.add(new Note(Integer.parseInt(parts[2]), new Photo(parts[1], ""), new Person(parts[0], "")));
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return notes;
    }
}
