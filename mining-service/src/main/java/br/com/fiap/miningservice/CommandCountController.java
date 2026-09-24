package br.com.fiap.miningservice;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CommandCountController {

    private final CommandCountRepository commandCountRepository;

    @GetMapping("/commands")
    public Map<String, Long> getCommandCounts() {
        Map<String, Long> counts = new LinkedHashMap<>();
        commandCountRepository.findAll().forEach(c -> counts.put(c.getCommand(), c.getTotal()));
        return counts;
    }

}
