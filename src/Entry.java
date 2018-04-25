public class Entry implements Comparable<Entry> {
public String name, number, notes;

Entry(String name, String number, String notes) {
    this.name = name;
    this.number = number;
    this.notes = notes;
}

@Override
public int compareTo(Entry o) {
    return name.compareTo(o.name);
}
}
