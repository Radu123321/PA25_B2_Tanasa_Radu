import java.util.*;

enum ProjectType
{
    THEORETICAL,
    PRACTICAL,
}

abstract  class Person
{
    protected  String name;
    protected  Date dateOfBirth;
    public Person(String name, Date dateOfBirth)
    {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
    }
    public String getName()
    {
        return name;
    }
    public void SetName(String name)
    {
        this.name = name;
    }
    public Date getDateOfBirth()
    {
        return dateOfBirth;
    }
    public void SetDateOfBirth(Date dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }
}

class Student extends Person
{
    private  String registrationNumber;
    public  Student(String name, Date dateOfBirth, String registrationNumber)
    {
        /// prin super apelam constructorul superclasei
        ///  adica clasa Person
        super(name, dateOfBirth);
        this.registrationNumber = registrationNumber;
    }
    public String getRegistrationNumber()
    {
        return registrationNumber;
    }
    public void setRegistrationNumber(String registrationNumber)
    {
        this.registrationNumber = registrationNumber;
    }
    @Override
    public boolean equals(Object obj)
    {
        /*
          super.equals(obj) apeleaza metoda equals suprascrisa
          in clasa parinte(Person).Vericam daca obiectele sunt egale
          comform regurilor definite in Person.
         */
        if(!super.equals(obj)) return false;
        Student student = (Student) obj;
        return Objects.equals(registrationNumber, student.registrationNumber);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), registrationNumber);
    }
    @Override
    public String toString()
    {
        return "Student{" + super.toString() + ", registrationNumber='" + registrationNumber + "'}";
    }
}

class Teacher extends Person
{
    private List<Project> proposedProjects = new ArrayList<>();
    public Teacher(String name, Date dateOfBirth)
    {
        super(name, dateOfBirth);
    }
    public void AddProjects(Project project)
    {
        if(!proposedProjects.contains(project))
            proposedProjects.add(project);
    }
    @Override
    public boolean equals(Object obj)
    {
        if (!super.equals(obj)) return false;
        Teacher teacher = (Teacher) obj;
        return Objects.equals(proposedProjects, teacher.proposedProjects);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), name);
    }
    @Override
    public String toString()
    {
        return "Teacher{" + super.toString() + ", proposedProjects=" + proposedProjects + "}";
    }
}

class Project
{
    private String title;
    private ProjectType type;
    private Teacher teacher;
    public Project(String title, ProjectType type, Teacher teacher)
    {
        this.title = title;
        this.type = type;
        this.teacher = teacher;
    }
    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }
    public ProjectType getType()
    {
        return type;
    }
    public void setType(ProjectType type)
    {
        this.type = type;
    }
    public Teacher getTeacher()
    {
        return teacher;
    }
    public void setTeacher(Teacher teacher)
    {
        this.teacher = teacher;
    }
    @Override
    public boolean equals(Object obj)
    {
        /*
         La fel ca mai sus, verificam daca this(clasa la care ne referim (this), (pointer alocat la zona de memorie,
         so obiectul obj sunt aceeasi)

         */
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Project project = (Project) obj;
        /*folosim metoda Object.equals la stringuri ca e mai sigur,
         (Verificare de egalitate a șirurilor (String) fără riscul de NullPointerException)
        */
        return  Objects.equals(title, project.title) && type == project.type &&
                Objects.equals(teacher, project.teacher);
    }
    @Override
    public int hashCode()
    {
        return Objects.hash(title, type, teacher != null ? teacher.getName() : null);
    }
    @Override
    public String toString()
    {
        return "Project{title='" + title + "', type=" + type + ", teacher=" + (teacher != null ? teacher.getName() : "None") + "}";
    }
}

class Problem
{
    private List<Student> students = new ArrayList<>();
    private List<Project> projects = new ArrayList<>();
    /*
       HashMap- reprezinta (asemanatoare)structura de date Map in c++
       vector de frecventa care verifica daca un element este pus sau nu
       in Map, in complexitate optima
    */
    private Map<Student, Project> allocations = new HashMap<>();
    private Map<Project, Student> matched = new HashMap<>();
    private Map<Student, Boolean> visited = new HashMap<>();
    private Map<Set<Student>, Integer> dpCache = new HashMap<>();
    public void addStudent(Student student)
    {
        if(!students.contains(student))
            students.add(student);
    }
    public void addProject(Project project)
    {
        if(!projects.contains(project))
            projects.add(project);
    }
    private boolean solve(Student student)
    {
        /*
        algoritmului lui Hopcroft Karp
        Complexitate O(radical din VE)
         */
        if (visited.getOrDefault(student, false)) return false;
        visited.put(student, true);
        for (Project project : projects)
        {
            if (!matched.containsKey(project))
            {
                matched.put(project, student);
                allocations.put(student, project);
                return true;
            }
        }
        for (Project project : projects)
        {
            Student previousStudent = matched.get(project);
            if (previousStudent != null && solve(previousStudent))
            {
                matched.put(project, student);
                allocations.put(student, project);
                return true;
            }
        }
        return false;
    }
    public void allocateProjects()
    {
        for (Student student : students)
        {
            visited.clear();
            solve(student);
        }
    }

    public void printAllocations()
    {
        for (Map.Entry<Student, Project> entry : allocations.entrySet())
            System.out.println(entry.getKey().getName() + " is assigned to " + entry.getValue().getTitle());
    }

    public boolean isMatchingPossible()
    {
        return checkHall(new HashSet<>(), 0);
    }
    private boolean checkHall(Set<Student> currentSet, int index)
    {
        if (index == students.size())
        {
            if (currentSet.isEmpty()) return true;
            if (dpCache.containsKey(currentSet)) return dpCache.get(currentSet) >= currentSet.size();
            int availableProjects = projects.size(); // Here, we assume all projects are available for all students
            dpCache.put(new HashSet<>(currentSet), availableProjects);
            return availableProjects >= currentSet.size();
        }

        boolean withoutCurrent = checkHall(currentSet, index + 1);
        currentSet.add(students.get(index));
        boolean withCurrent = checkHall(currentSet, index + 1);
        currentSet.remove(students.get(index));

        return withoutCurrent && withCurrent;
    }

    public void printResult()
    {
        System.out.println("Matching possible: " + isMatchingPossible());
    }
}

public class Main {
    public static void main(String[] args)
    {
        /*Teacher teacher = new Teacher("Vasile Ion", new Date());
        Project p1 = new Project("Practica SGBD", ProjectType.THEORETICAL, teacher);
        Project p2 = new Project("Dezvoltare web", ProjectType.PRACTICAL, teacher);
        Project p3 = new Project("Programare avansata", ProjectType.PRACTICAL, teacher);
        teacher.AddProjects(p1);
        teacher.AddProjects(p2);
        teacher.AddProjects(p3);
        Student student1 = new Student("Stefan", new Date(), "S123");
        Student student2 = new Student("Laurentiu", new Date(), "S124");
        Student student3 = new Student("George", new Date(), "S125");
        Problem problem = new Problem();
        problem.addProject(p1);
        problem.addProject(p2);
        problem.addProject(p3);
        problem.addStudent(student1);
        problem.addStudent(student2);
        problem.addStudent(student3);
        problem.allocateProjects();
        problem.printAllocations();
         */
        Problem problem = new Problem();
        Teacher teacher = new Teacher("Vasile Ion", new Date());
        Project p1 = new Project("Practica SGBD", ProjectType.THEORETICAL, teacher);
        Project p2 = new Project("Dezvoltare web", ProjectType.PRACTICAL, teacher);
        teacher.AddProjects(p1);
        teacher.AddProjects(p2);
        Student student1 = new Student("Stefan", new Date(), "S123");
        Student student2 = new Student("Laurentiu", new Date(), "S124");
        Student student3 = new Student("George", new Date(), "S125");
        problem.printResult();
    }
}
