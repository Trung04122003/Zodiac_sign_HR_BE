package com.jci.zodiac.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jci.zodiac.entity.*;
import com.jci.zodiac.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * DataImportService - Import system data from JSON backup files
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DataImportService {

    private final MemberRepository memberRepository;
    private final DepartmentRepository departmentRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final NoteRepository noteRepository;
    private final SettingRepository settingRepository;
    private final ZodiacProfileRepository zodiacProfileRepository;
    private final ZodiacCompatibilityRepository compatibilityRepository;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    /**
     * Import all data from JSON backup file
     */
    @Transactional
    public Map<String, Object> importDataFromJson(MultipartFile file) {
        log.info("Importing data from JSON file: {}", file.getOriginalFilename());

        Map<String, Object> result = new HashMap<>();
        Map<String, Integer> importCounts = new HashMap<>();
        List<String> errors = new ArrayList<>();

        try {
            // Parse JSON file
            Map<String, Object> backupData = objectMapper.readValue(
                    file.getInputStream(),
                    new TypeReference<Map<String, Object>>() {}
            );

            log.info("Backup file parsed successfully");

            // Import settings first (they might be needed by other entities)
            if (backupData.containsKey("settings")) {
                int count = importSettings(backupData.get("settings"));
                importCounts.put("settings", count);
            }

            // Import departments
            if (backupData.containsKey("departments")) {
                int count = importDepartments(backupData.get("departments"));
                importCounts.put("departments", count);
            }

            // Import members
            if (backupData.containsKey("members")) {
                int count = importMembers(backupData.get("members"));
                importCounts.put("members", count);
            }

            // Import teams
            if (backupData.containsKey("teams")) {
                int count = importTeams(backupData.get("teams"));
                importCounts.put("teams", count);
            }

            // Import team members
            if (backupData.containsKey("teamMembers")) {
                int count = importTeamMembers(backupData.get("teamMembers"));
                importCounts.put("teamMembers", count);
            }

            // Import notes
            if (backupData.containsKey("notes")) {
                int count = importNotes(backupData.get("notes"));
                importCounts.put("notes", count);
            }

            result.put("success", true);
            result.put("importCounts", importCounts);
            result.put("totalImported", importCounts.values().stream().mapToInt(Integer::intValue).sum());
            result.put("errors", errors);

            log.info("Data import completed successfully. Total imported: {}",
                    result.get("totalImported"));

        } catch (Exception e) {
            log.error("Error importing data from JSON: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("error", e.getMessage());
            errors.add("Fatal error: " + e.getMessage());
            result.put("errors", errors);
        }

        return result;
    }

    /**
     * Import only settings from JSON file
     */
    @Transactional
    public Map<String, Object> importSettingsFromJson(MultipartFile file) {
        log.info("Importing settings from JSON file: {}", file.getOriginalFilename());

        Map<String, Object> result = new HashMap<>();

        try {
            Map<String, Object> data = objectMapper.readValue(
                    file.getInputStream(),
                    new TypeReference<Map<String, Object>>() {}
            );

            int count = 0;
            if (data.containsKey("settings")) {
                count = importSettings(data.get("settings"));
            }

            result.put("success", true);
            result.put("importedCount", count);
            log.info("Settings import completed. Imported: {}", count);

        } catch (Exception e) {
            log.error("Error importing settings: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }

        return result;
    }

    // ==================== Import Helper Methods ====================

    private int importSettings(Object settingsData) {
        log.debug("Importing settings...");

        try {
            List<Setting> settings = objectMapper.convertValue(
                    settingsData,
                    new TypeReference<List<Setting>>() {}
            );

            int count = 0;
            for (Setting setting : settings) {
                try {
                    // Check if setting exists
                    Optional<Setting> existing = settingRepository.findByKey(setting.getKey());

                    if (existing.isPresent()) {
                        // Update existing if editable
                        Setting existingSetting = existing.get();
                        if (Boolean.TRUE.equals(existingSetting.getIsEditable())) {
                            existingSetting.setValue(setting.getValue());
                            existingSetting.setDescription(setting.getDescription());
                            settingRepository.save(existingSetting);
                            count++;
                        }
                    } else {
                        // Create new
                        settingRepository.save(setting);
                        count++;
                    }
                } catch (Exception e) {
                    log.warn("Failed to import setting {}: {}", setting.getKey(), e.getMessage());
                }
            }

            log.info("Imported {} settings", count);
            return count;

        } catch (Exception e) {
            log.error("Error importing settings: {}", e.getMessage());
            return 0;
        }
    }

    private int importDepartments(Object departmentsData) {
        log.debug("Importing departments...");

        try {
            List<Department> departments = objectMapper.convertValue(
                    departmentsData,
                    new TypeReference<List<Department>>() {}
            );

            int count = 0;
            for (Department dept : departments) {
                try {
                    if (!departmentRepository.existsByCode(dept.getCode())) {
                        departmentRepository.save(dept);
                        count++;
                    }
                } catch (Exception e) {
                    log.warn("Failed to import department {}: {}", dept.getCode(), e.getMessage());
                }
            }

            log.info("Imported {} departments", count);
            return count;

        } catch (Exception e) {
            log.error("Error importing departments: {}", e.getMessage());
            return 0;
        }
    }

    private int importMembers(Object membersData) {
        log.debug("Importing members...");

        try {
            List<Member> members = objectMapper.convertValue(
                    membersData,
                    new TypeReference<List<Member>>() {}
            );

            int count = 0;
            for (Member member : members) {
                try {
                    if (!memberRepository.existsByMemberCode(member.getMemberCode())) {
                        memberRepository.save(member);
                        count++;
                    }
                } catch (Exception e) {
                    log.warn("Failed to import member {}: {}", member.getMemberCode(), e.getMessage());
                }
            }

            log.info("Imported {} members", count);
            return count;

        } catch (Exception e) {
            log.error("Error importing members: {}", e.getMessage());
            return 0;
        }
    }

    private int importTeams(Object teamsData) {
        log.debug("Importing teams...");

        try {
            List<Team> teams = objectMapper.convertValue(
                    teamsData,
                    new TypeReference<List<Team>>() {}
            );

            int count = 0;
            for (Team team : teams) {
                try {
                    teamRepository.save(team);
                    count++;
                } catch (Exception e) {
                    log.warn("Failed to import team {}: {}", team.getName(), e.getMessage());
                }
            }

            log.info("Imported {} teams", count);
            return count;

        } catch (Exception e) {
            log.error("Error importing teams: {}", e.getMessage());
            return 0;
        }
    }

    private int importTeamMembers(Object teamMembersData) {
        log.debug("Importing team members...");

        try {
            List<TeamMember> teamMembers = objectMapper.convertValue(
                    teamMembersData,
                    new TypeReference<List<TeamMember>>() {}
            );

            int count = 0;
            for (TeamMember tm : teamMembers) {
                try {
                    teamMemberRepository.save(tm);
                    count++;
                } catch (Exception e) {
                    log.warn("Failed to import team member: {}", e.getMessage());
                }
            }

            log.info("Imported {} team members", count);
            return count;

        } catch (Exception e) {
            log.error("Error importing team members: {}", e.getMessage());
            return 0;
        }
    }

    private int importNotes(Object notesData) {
        log.debug("Importing notes...");

        try {
            List<Note> notes = objectMapper.convertValue(
                    notesData,
                    new TypeReference<List<Note>>() {}
            );

            int count = 0;
            for (Note note : notes) {
                try {
                    noteRepository.save(note);
                    count++;
                } catch (Exception e) {
                    log.warn("Failed to import note: {}", e.getMessage());
                }
            }

            log.info("Imported {} notes", count);
            return count;

        } catch (Exception e) {
            log.error("Error importing notes: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * Validate backup file structure
     */
    public boolean validateBackupFile(MultipartFile file) {
        try {
            Map<String, Object> data = objectMapper.readValue(
                    file.getInputStream(),
                    new TypeReference<Map<String, Object>>() {}
            );

            // Check if it has expected structure
            return data.containsKey("exportDate") ||
                    data.containsKey("settings") ||
                    data.containsKey("members");

        } catch (Exception e) {
            log.error("Invalid backup file: {}", e.getMessage());
            return false;
        }
    }
}