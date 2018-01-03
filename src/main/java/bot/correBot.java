package bot;

public class correBot {

    public static void main(String[] args) throws InterruptedException {
        Thread thread[] = new Thread[10];

        for(int i=0; i<10;i++){
            thread[i] = new Thread(new queueUp(i,i));
        }

        for(int i=0; i<10;i++){
            thread[i].start();
        }
    }
}
