import easyaccept.EasyAccept;

public class Main {
    public static void main(String[] args) {
        String[] args2 = {"br.ufal.ic.p2.jackut.Facade",
               "tests/us1_1.txt", "tests/us1_2.txt",
        };

        String[] args3 = {"br.ufal.ic.p2.jackut.Facade",
                "tests/us1_2.txt",
        };

        String[] args4 = {"br.ufal.ic.p2.jackut.Facade",
                "tests/us2_1.txt"
        };

        String[] args5 = {"br.ufal.ic.p2.jackut.Facade",
                "tests/us2_2.txt"
        };

        String[] args6 = {"br.ufal.ic.p2.jackut.Facade",
                "tests/us3_1.txt"
        };

        String[] args7 = {"br.ufal.ic.p2.jackut.Facade",
                "tests/us3_2.txt"
        };

        String[] args8 = {"br.ufal.ic.p2.jackut.Facade",
                "tests/us4_1.txt"
        };


        String[] args9 = {"br.ufal.ic.p2.jackut.Facade",
                "tests/us4_2.txt"

        };


        EasyAccept.main(args2);
        EasyAccept.main(args3);
        EasyAccept.main(args4);
        EasyAccept.main(args5);
        EasyAccept.main(args6);
        EasyAccept.main(args7);
        EasyAccept.main(args8);
        EasyAccept.main(args9);
    }
}

