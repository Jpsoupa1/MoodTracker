import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Comparator; 
import java.util.Collections;

public class MoodTrackerApp {

   
    private static List<Mood> moodEntries = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static final String FILE_NAME = "moods.txt"; 

    public static void main(String[] args) {
        System.out.println("--- Bem-vindo ao MoodTracker! ---");
        loadMoodsFromFile();

        
        while (true) {
            displayMenu();
            String menuOption = scanner.nextLine().trim();

            switch (menuOption) {
                case "a":
                    addMood();
                    break;
                case "d":
                    deleteMood();
                    break;
                case "e":
                    editMood();
                    break;
                case "s":
                    searchMoods();
                    break;
                case "M":
                    getAllMoods();
                    break;
                case "w":
                    writeMoodsToFile();
                    break;
                case "Exit":
                    System.out.println("Obrigado por usar o MoodTracker. Até mais!");
                    saveMoodsToFile();
                    scanner.close();
                    return; 
                default:
                    System.out.println("Entrada inválida. Por favor, escolha uma opção do menu.");
            }
            System.out.println("\nPressione Enter para continuar...");
            scanner.nextLine();
        }
    }


    private static void displayMenu() {
        System.out.println("\n--- MENU ---");
        System.out.println("a: Adicionar humor");
        System.out.println("d: Excluir humor(es)");
        System.out.println("e: Editar humor");
        System.out.println("s: Buscar humores");
        System.out.println("M: Ver todos os humores");
        System.out.println("w: Salvar humores em arquivo");
        System.out.println("Type 'Exit' para sair");
        System.out.print("Escolha uma opção: ");
    }

    private static void addMood() {
        System.out.println("\n--- Adicionar Novo Humor ---");
        System.out.print("Digite o nome do humor (ex: Feliz, Triste, Ansioso): ");
        String name = scanner.nextLine().trim();

        LocalDate date = LocalDate.now(); 
        LocalTime time = LocalTime.now(); 
        String notes = ""; 

        System.out.print("Deseja inserir uma data específica? (s/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
            System.out.print("Digite a data (AAAA-MM-DD): ");
            try {
                date = LocalDate.parse(scanner.nextLine().trim());
            } catch (DateTimeParseException e) {
                System.out.println("Formato de data inválido. Usando a data atual.");
            }
        }

        System.out.print("Deseja inserir uma hora específica? (s/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
            System.out.print("Digite a hora (HH:MM ou HH:MM:SS): ");
            try {
                time = LocalTime.parse(scanner.nextLine().trim());
            } catch (DateTimeParseException e) {
                System.out.println("Formato de hora inválido. Usando a hora atual.");
            }
        }

        System.out.print("Digite notas adicionais (opcional, ENTER para pular): ");
        notes = scanner.nextLine().trim();

        Mood newMood = new Mood(name, date, time, notes);
        moodEntries.add(newMood);
        System.out.println("Humor '" + name + "' adicionado com sucesso!");
    }

    private static void deleteMood() {
        System.out.println("\n--- Excluir Humor(es) ---");
        if (moodEntries.isEmpty()) {
            System.out.println("Nenhum humor para excluir. A lista está vazia.");
            return;
        }

        getAllMoods();
        System.out.print("Digite o NÚMERO do humor a ser excluído (ou 0 para cancelar): ");
        try {
            int indexToDelete = scanner.nextInt();
            scanner.nextLine(); 

            if (indexToDelete == 0) {
                System.out.println("Exclusão cancelada.");
                return;
            }

            if (indexToDelete > 0 && indexToDelete <= moodEntries.size()) {
                Mood removedMood = moodEntries.remove(indexToDelete - 1); 
                System.out.println("Humor '" + removedMood.getName() + "' excluído com sucesso!");
            } else {
                System.out.println("Número inválido. Por favor, digite um número da lista.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, digite um número.");
            scanner.nextLine();
        }
    }

    private static void editMood() {
        System.out.println("\n--- Editar Humor ---");
        if (moodEntries.isEmpty()) {
            System.out.println("Nenhum humor para editar. A lista está vazia.");
            return;
        }

        getAllMoods();
        System.out.print("Digite o NÚMERO do humor a ser editado (ou 0 para cancelar): ");
        try {
            int indexToEdit = scanner.nextInt();
            scanner.nextLine();

            if (indexToEdit == 0) {
                System.out.println("Edição cancelada.");
                return;
            }

            if (indexToEdit > 0 && indexToEdit <= moodEntries.size()) {
                Mood moodToEdit = moodEntries.get(indexToEdit - 1);
                System.out.println("Editando: " + moodToEdit);

                System.out.print("Novo nome do humor (ENTER para manter '" + moodToEdit.getName() + "'): ");
                String newName = scanner.nextLine().trim();
                if (!newName.isEmpty()) {
                    moodToEdit.setName(newName);
                }

                System.out.print("Nova data (AAAA-MM-DD, ENTER para manter " + moodToEdit.getDate() + "): ");
                String dateStr = scanner.nextLine().trim();
                if (!dateStr.isEmpty()) {
                    try {
                        moodToEdit.setDate(LocalDate.parse(dateStr));
                    } catch (DateTimeParseException e) {
                        System.out.println("Formato de data inválido. Mantendo data anterior.");
                    }
                }

                System.out.print("Nova hora (HH:MM ou HH:MM:SS, ENTER para manter " + moodToEdit.getTime() + "): ");
                String timeStr = scanner.nextLine().trim();
                if (!timeStr.isEmpty()) {
                    try {
                        moodToEdit.setTime(LocalTime.parse(timeStr));
                    } catch (DateTimeParseException e) {
                        System.out.println("Formato de hora inválido. Mantendo hora anterior.");
                    }
                }

                System.out.print("Novas notas (ENTER para manter ou deixar vazio): ");
                String newNotes = scanner.nextLine().trim();
                moodToEdit.setNotes(newNotes);

                System.out.println("Humor editado com sucesso!");
                System.out.println("Novo estado: " + moodToEdit);

            } else {
                System.out.println("Número inválido. Por favor, digite um número da lista.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, digite um número.");
            scanner.nextLine();
        }
    }

    private static void searchMoods() {
        System.out.println("\n--- Buscar Humores ---");
        if (moodEntries.isEmpty()) {
            System.out.println("Nenhum humor para buscar. A lista está vazia.");
            return;
        }

        System.out.print("Digite o nome do humor para buscar: ");
        String searchTerm = scanner.nextLine().trim();

        List<Mood> foundMoods = new ArrayList<>();
        for (Mood mood : moodEntries) {
            if (mood.getName().equalsIgnoreCase(searchTerm)) {
                foundMoods.add(mood);
            }
        }

        if (foundMoods.isEmpty()) {
            System.out.println("Nenhum humor encontrado com o nome '" + searchTerm + "'.");
        } else {
            System.out.println("Humores encontrados para '" + searchTerm + "':");
            for (Mood mood : foundMoods) {
                System.out.println(mood);
            }
        }
    }

    private static void getAllMoods() {
        System.out.println("\n--- Todos os Humores Registrados ---");
        if (moodEntries.isEmpty()) {
            System.out.println("Nenhum humor registrado ainda. A lista está vazia.");
            return;
        }

        
        Collections.sort(moodEntries, new Comparator<Mood>() {
            @Override
            public int compare(Mood m1, Mood m2) {
                int dateComparison = m1.getDate().compareTo(m2.getDate());
                if (dateComparison == 0) {
                    return m1.getTime().compareTo(m2.getTime());
                }
                return dateComparison;
            }
        });

        for (int i = 0; i < moodEntries.size(); i++) {
            System.out.println((i + 1) + ". " + moodEntries.get(i));
        }
    }

    private static void saveMoodsToFile() {
        try {
            Path filePath = Paths.get(FILE_NAME);
            List<String> lines = new ArrayList<>();
            for (Mood mood : moodEntries) {
                lines.add(String.format("%s;%s;%s;%s",
                    mood.getName(),
                    mood.getDate().toString(),
                    mood.getTime().toString(),
                    mood.getNotes() != null ? mood.getNotes().replace("\n", "\\n") : ""
                ));
            }
            Files.write(filePath, lines);
            System.out.println("Humores salvos em '" + FILE_NAME + "' com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao salvar humores no arquivo: " + e.getMessage());
        }
    }


    private static void writeMoodsToFile() {
        saveMoodsToFile(); 
    }

    private static void loadMoodsFromFile() {
        Path filePath = Paths.get(FILE_NAME);
        if (Files.exists(filePath)) {
            try {
                List<String> lines = Files.readAllLines(filePath);
                moodEntries.clear();
                for (String line : lines) {
                    String[] parts = line.split(";");
                    if (parts.length == 4) { 
                        String name = parts[0];
                        LocalDate date = LocalDate.parse(parts[1]);
                        LocalTime time = LocalTime.parse(parts[2]);
                        String notes = parts[3].replace("\\n", "\n");

                        moodEntries.add(new Mood(name, date, time, notes));
                    }
                }
                System.out.println("Humores carregados de '" + FILE_NAME + "' com sucesso!");
            } catch (IOException e) {
                System.err.println("Erro ao carregar humores do arquivo: " + e.getMessage());
            } catch (DateTimeParseException e) {
                System.err.println("Erro de formato de data/hora ao carregar humores: " + e.getMessage());
            }
        } else {
            System.out.println("Arquivo de humores não encontrado. Iniciando com lista vazia.");
        }
    }
}