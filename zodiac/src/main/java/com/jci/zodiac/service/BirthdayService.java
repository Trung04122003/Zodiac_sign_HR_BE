package com.jci.zodiac.service;

import com.jci.zodiac.dto.response.MemberSummaryResponse;
import com.jci.zodiac.entity.Member;
import com.jci.zodiac.mapper.MemberMapper;
import com.jci.zodiac.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

/**
 * BirthdayService - Birthday tracking and reminders
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BirthdayService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    /**
     * Get members with birthdays today
     */
    @Transactional(readOnly = true)
    public List<MemberSummaryResponse> getBirthdaysToday() {
        log.debug("Fetching birthdays today");

        LocalDate today = LocalDate.now();
        List<Member> members = memberRepository.findByBirthday(
                today.getMonthValue(),
                today.getDayOfMonth()
        );

        return members.stream()
                .map(memberMapper::toSummaryResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get upcoming birthdays in next N days
     */
    @Transactional(readOnly = true)
    public List<MemberSummaryResponse> getUpcomingBirthdays(int daysAhead) {
        log.debug("Fetching upcoming birthdays in next {} days", daysAhead);

        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(daysAhead);

        List<Member> members = memberRepository.findUpcomingBirthdays(today, endDate);

        return members.stream()
                .map(memberMapper::toSummaryResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get birthdays in a specific month
     */
    @Transactional(readOnly = true)
    public List<MemberSummaryResponse> getBirthdaysByMonth(Month month) {
        log.debug("Fetching birthdays in month: {}", month);

        List<Member> allMembers = memberRepository.findByMembershipStatus(Member.MembershipStatus.Active);

        return allMembers.stream()
                .filter(member -> member.getDateOfBirth().getMonth() == month)
                .sorted((m1, m2) -> Integer.compare(
                        m1.getDateOfBirth().getDayOfMonth(),
                        m2.getDateOfBirth().getDayOfMonth()
                ))
                .map(memberMapper::toSummaryResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get birthdays by zodiac sign
     */
    @Transactional(readOnly = true)
    public List<MemberSummaryResponse> getBirthdaysByZodiacSign(Member.ZodiacSign sign) {
        log.debug("Fetching birthdays for zodiac sign: {}", sign);

        List<Member> members = memberRepository.findByZodiacSign(sign);

        return members.stream()
                .sorted((m1, m2) -> {
                    int monthCompare = m1.getDateOfBirth().getMonth().compareTo(m2.getDateOfBirth().getMonth());
                    if (monthCompare != 0) return monthCompare;
                    return Integer.compare(
                            m1.getDateOfBirth().getDayOfMonth(),
                            m2.getDateOfBirth().getDayOfMonth()
                    );
                })
                .map(memberMapper::toSummaryResponse)
                .collect(Collectors.toList());
    }

    /**
     * Check if today is anyone's birthday
     */
    @Transactional(readOnly = true)
    public boolean hasBirthdaysToday() {
        LocalDate today = LocalDate.now();
        return !memberRepository.findByBirthday(
                today.getMonthValue(),
                today.getDayOfMonth()
        ).isEmpty();
    }

    /**
     * Get count of upcoming birthdays
     */
    @Transactional(readOnly = true)
    public long countUpcomingBirthdays(int daysAhead) {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(daysAhead);
        return memberRepository.findUpcomingBirthdays(today, endDate).size();
    }
}