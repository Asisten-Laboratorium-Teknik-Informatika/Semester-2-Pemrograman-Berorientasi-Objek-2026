package util;

public class Loading {

    public static void tampil(
            String pesan
    ) {

        try {

            System.out.print(
                    "\n" + pesan
            );

            for (int i = 0; i < 3; i++) {

                Thread.sleep(500);

                System.out.print(".");
            }

            Thread.sleep(500);

            System.out.println();

        } catch (InterruptedException e) {

            e.printStackTrace();
        }
    }

    public static void sukses(
            String pesan
    ) {

        try {

            System.out.print(
                    "\n" + pesan
            );

            Thread.sleep(700);

            System.out.println(
                    " berhasil!"
            );

        } catch (InterruptedException e) {

            e.printStackTrace();
        }
    }
}