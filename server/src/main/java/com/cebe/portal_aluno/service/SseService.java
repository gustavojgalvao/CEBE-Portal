package com.cebe.portal_aluno.service;

import com.cebe.portal_aluno.entity.MensagemAtendimento;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SseService {

    // Maps atendimentoId to a list of SseEmitters
    private final ConcurrentHashMap<Integer, List<SseEmitter>> emittersMap = new ConcurrentHashMap<>();

    public SseEmitter subscribe(Integer atendimentoId) {
        // Set timeout to 30 minutes (or use 0L for infinite, but infinite can cause memory leaks if not careful)
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L);

        List<SseEmitter> emitters = emittersMap.computeIfAbsent(atendimentoId, k -> new CopyOnWriteArrayList<>());
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError((e) -> emitters.remove(emitter));

        return emitter;
    }

    public void notifySubscribers(Integer atendimentoId, MensagemAtendimento mensagem) {
        List<SseEmitter> emitters = emittersMap.get(atendimentoId);
        if (emitters != null) {
            for (SseEmitter emitter : emitters) {
                try {
                    emitter.send(SseEmitter.event()
                            .name("nova-mensagem")
                            .data(mensagem));
                } catch (IOException e) {
                    emitter.complete();
                    emitters.remove(emitter);
                }
            }
        }
    }
}
