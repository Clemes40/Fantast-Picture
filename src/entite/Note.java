package entite;

public class Note {
	private int note;
	private Photo photo;
	private Person personne;
	
	public Note(int note, Photo photo, Person personne) {
		this.note = note;
		this.photo = photo;
		this.personne = personne;
	}
	
	public int getNote() {
		return note;
	}
	public void setNote(int note) {
		this.note = note;
	}
	public Photo getPhoto() {
		return photo;
	}
	public void setPhoto(Photo photo) {
		this.photo = photo;
	}
	public Person getPersonne() {
		return personne;
	}
	public void setPersonne(Person personne) {
		this.personne = personne;
	}
}
