package com.PSPAsshole;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Random;

public class Listener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent ev) {

        try {
            Message ms = ev.getMessage();

            String msg = ms.getContentRaw();
            String[] args = msg.split(" ");

            if (args[0].equalsIgnoreCase("도배")) {

                if (args.length == 1) return;

                User tg = null;

                try {

                    tg = ev.getJDA().getUserByTag(args[1]);

                } catch (IllegalStateException e) {
                    // ignore (tg = null)
                }

                if (args.length == 2) return;

                if (tg == null) {
                    try {
                        tg = ms.getMentionedMembers().get(0).getUser();

                        if (tg == null) {
                            ev.getChannel().sendMessage("해당 유저가 없습니다.").queue();
                            return;
                        }
                    } catch (NullPointerException ex) {
                        ev.getChannel().sendMessage("해당 유저가 없습니다.").queue();
                        return;
                    }
                }

                int reg = Integer.parseInt(args[2]);

                Message msgR = ev.getChannel().sendMessage("성공적으로 보내진 메시지 개수 : 0개").complete();

                for (int i = 0; i < reg; i++) {
                    int finalI = i;

                    String text = String.join(" ", Arrays.copyOfRange(args, 3, args.length))
                            .replace("{i}", String.valueOf(i + 1))
                            .replace("{r}", String.valueOf(new Random().nextInt(100) + 1));
                    tg.openPrivateChannel().complete().sendMessage(text).queue(
                            it -> msgR.editMessage(finalI != reg - 1 ? "성공적으로 보내진 메시지 개수 : " + (finalI + 1) + "개" : "모든 메시지를 성공적으로 전송했습니다.").queue()
                    );
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
