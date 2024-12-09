package com.radlane.payment.repository;


import com.radlane.payment.model.entity.CryptoChannel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ChannelRepositoryTest {

    @Autowired
    private ChannelRepository channelRepository;

    @Test
    public void testSaveAndFindById() {
        // Given
        CryptoChannel channel = new CryptoChannel();
        channel.setName("Bitcoin Channel");

        // When
        CryptoChannel savedChannel = channelRepository.save(channel);

        // Then
        assertThat(savedChannel).isNotNull();
        assertThat(savedChannel.getId()).isNotNull();
        assertThat(channelRepository.findById(savedChannel.getId())).isPresent();
    }

    @Test
    public void testFindByName() {
        // Given
        CryptoChannel channel = new CryptoChannel();
        channel.setName("Ethereum Channel");
        channelRepository.save(channel);

        // When
        CryptoChannel foundChannel = channelRepository.findById(channel.getId()).orElse(null);

        // Then
        assertThat(foundChannel).isNotNull();
        assertThat(foundChannel.getName()).isEqualTo("Ethereum Channel");
    }
}
