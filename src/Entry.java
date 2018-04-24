public class Entry {
public String codeName, number, notes;

Entry(String codeName, String number, String notes) {
    this.codeName = codeName;
    this.number = number;
    this.notes = notes;
}

@Override
public String toString() {
    return "-- " + codeName + "\n-- " + number + "\n-- " + notes;
}
}
