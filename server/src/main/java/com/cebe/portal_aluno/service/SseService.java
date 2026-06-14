package com.cebe.portal_aluno.service;

import com.cebe.portal_aluno.entity.MensagemAtendimento;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SseService {

    private final ConcurrentHashMap<Integer, List<SseEmitter>> emittersMap = new ConcurrentHashMap<>();

    public SseEmitter subscribe(Integer atendimentoId) {
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L); // 30 minutos de timeout

        List<SseEmitter> lista = emittersMap.computeIfAbsent(atendimentoId, k -> new CopyOnWriteArrayList<>());
        lista.add(emitter);

        emitter.onCompletion(() -> lista.remove(emitter));
        emitter.onTimeout(() -> lista.remove(emitter));
        emitter.onError(e -> lista.remove(emitter));

        return emitter;
    }

    public void notifySubscribers(Integer atendimentoId, MensagemAtendimento mensagem) {
        List<SseEmitter> lista = emittersMap.get(atendimentoId);
        if (lista == null) return;

        List<SseEmitter> mortos = new ArrayList<>();
        for (SseEmitter emitter : lista) {
            try {
                emitter.send(SseEmitter.event().name("nova-mensagem").data(mensagem));
            } catch (IOException e) {
                mortos.add(emitter);
            }
        }
        lista.removeAll(mortos);
    }
}
