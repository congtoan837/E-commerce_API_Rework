package com.poly.services.imp;

import com.poly.entity.Image;
import com.poly.repositories.ImageRepository;
import com.poly.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImageServiceImp implements ImageService {
    @Autowired
    ImageRepository imageRepository;

    @Override
    public Page<Image> findAll(Pageable pageable) {
        return imageRepository.findAll(pageable);
    }

    @Override
    public Image save(Image entity) {
        return imageRepository.save(entity);
    }

    @Override
    public Optional<Image> findById(Long aLong) {
        return imageRepository.findById(aLong);
    }

    @Override
    public long count() {
        return imageRepository.count();
    }

    @Override
    public void deleteById(Long aLong) {
        imageRepository.deleteById(aLong);
    }
}
