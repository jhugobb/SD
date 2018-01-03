package bot;

public class correBot {

    public static void main(String[] args) {
        Thread thread[] = new Thread[1000];

        for(int i=0; i<1000;i++){
            thread[i] = new Thread(new queueUp(i,0));
        }

        for(int i=0; i<1000;i++){
            thread[i].start();
        }
    }
}
