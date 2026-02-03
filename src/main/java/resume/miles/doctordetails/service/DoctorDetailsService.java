package resume.miles.doctordetails.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PreDestroy;
import resume.miles.doctorabout.entity.DoctorAboutEntity;
import resume.miles.doctorabout.mapper.DoctorAboutMapper;
import resume.miles.doctorabout.repository.DoctorAboutRepository;
import resume.miles.doctordetails.dto.DetailsResponseDTO;
import resume.miles.doctordetails.helper.GovDocImageUploadHelper;
import resume.miles.doctordetails.helper.ImageRegistrationHelper;
import resume.miles.doctorid.entity.DoctorIdEntity;
import resume.miles.doctorid.mapper.DoctorIdMapper;
import resume.miles.doctorid.repository.DoctorIdRepository;
import resume.miles.doctorspecialization.entity.DoctorSpecializationEntity;

import resume.miles.doctorspecialization.repository.SpecializationRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorDetailsService {

    private final DoctorAboutRepository doctorAboutRepository;

    private final DoctorIdRepository doctorIdRepository;

    private final SpecializationRepository specializationRepository;

    private final GovDocImageUploadHelper govDocImageUploadHelper;

    private final ImageRegistrationHelper imageRegistrationHelper;

    private final TransactionTemplate transactionTemplate;

    private final ExecutorService customExecutor = Executors.newFixedThreadPool(4);

    public String create(Long id,MultipartFile file,MultipartFile docFile,DetailsResponseDTO detailsResponseDTO){
        // ExecutorService customExecutor = Executors.newFixedThreadPool(4);
        try{
            CompletableFuture<String> fileFuture = CompletableFuture.supplyAsync(() -> {
                if (docFile == null || docFile.isEmpty()) {
                    return null; 
                }
                try {
                    String data = govDocImageUploadHelper.saveFile(docFile);
                    return data;
                } catch (IOException e) {
                    throw new RuntimeException("Failed to save file", e);
                }
            },customExecutor);

            CompletableFuture<String> docFuture = CompletableFuture.supplyAsync(() -> {
                if (file == null || file.isEmpty()) {
                    return null;
                }
                try {
                    String data = imageRegistrationHelper.saveFile(file);
                    return data;
                } catch (IOException e) {
                    throw new RuntimeException("Failed to save file", e);
                }
            },customExecutor);

            CompletableFuture<Void> dbCheckFutureAbout = CompletableFuture.runAsync(() -> {
                transactionTemplate.execute(status -> {
                    DoctorAboutEntity exists = doctorAboutRepository.findByDoctorId(id).map(exsit->{
                        if (detailsResponseDTO.getAbout().getAbout() != null 
                                && !detailsResponseDTO.getAbout().getAbout().isEmpty()) {
                                exsit.setAbout(detailsResponseDTO.getAbout().getAbout());
                        }
                    
                        if (detailsResponseDTO.getAbout().getLanguage() != null 
                            && !detailsResponseDTO.getAbout().getLanguage().isEmpty()) {
                            exsit.setLanguage(detailsResponseDTO.getAbout().getLanguage());
                        }
                        if (detailsResponseDTO.getAbout().getExp() != null ) {
                            exsit.setExp(detailsResponseDTO.getAbout().getExp());
                        }
                        doctorAboutRepository.save(exsit);
                        return exsit;
                    }).orElseGet(()->{
                        DoctorAboutEntity data = DoctorAboutMapper.toDoctorAboutEntity(detailsResponseDTO.getAbout());
                        data.setDoctorId(id);
                        data.setStatus(1);
                        return doctorAboutRepository.save(data);
                    });
                    return null;
                });
                
               
            }, customExecutor);

            CompletableFuture<Void> dbCheckFutureId = fileFuture.thenCombineAsync(docFuture, (govDocPath, regImagePath) -> {
                transactionTemplate.execute(status -> {
                    doctorIdRepository.findByDoctorId(id)
                        .map(existing -> {
                            if (detailsResponseDTO.getIdentification().getIdName() != null 
                                && !detailsResponseDTO.getIdentification().getIdName().isEmpty()) {
                                existing.setIdName(detailsResponseDTO.getIdentification().getIdName());
                            }
                            if (detailsResponseDTO.getIdentification().getRegistrationNo() != null 
                                && !detailsResponseDTO.getIdentification().getRegistrationNo().isEmpty()) {
                                
                                existing.setRegistrationNo(detailsResponseDTO.getIdentification().getRegistrationNo());
                            }
                            if (govDocPath != null) {
                                String oldGovDocPath = existing.getIdImage();
                                if (oldGovDocPath != null && !oldGovDocPath.isEmpty()) {
                                    try {
                                        govDocImageUploadHelper.deleteFile(oldGovDocPath); 
                                    } catch (Exception e) {
                                        System.err.println("Warning: Could not delete old Gov ID: " + e.getMessage());
                                    }
                                }
                                existing.setIdImage(govDocPath);  
                            }
                            if (regImagePath != null) {
                                String oldRegPath = existing.getRegistrationNoImage();
                                if (oldRegPath != null && !oldRegPath.isEmpty()) {
                                    try {
                                        imageRegistrationHelper.deleteFile(oldRegPath);
                                    } catch (Exception e) {
                                        System.err.println("Warning: Could not delete old Reg Image: " + e.getMessage());
                                    }
                                }
                                existing.setRegistrationNoImage(regImagePath); 
                            }
                      
                            return doctorIdRepository.save(existing);
                        })
                        .orElseGet(() -> {
                            DoctorIdEntity newEntity = DoctorIdMapper.toEntity(detailsResponseDTO.getIdentification());
                            newEntity.setStatus(1);
                            newEntity.setDoctorId(id);
                            if (govDocPath != null) {
                                newEntity.setIdImage(govDocPath);  
                            }
                            if (regImagePath != null) {
                                newEntity.setRegistrationNoImage(regImagePath); 
                            }
                            return doctorIdRepository.save(newEntity);
                        });

                        return null;
                    });
                  
                    return null; 
                }, customExecutor);

            CompletableFuture<Void> specializations = CompletableFuture.runAsync(() -> {
               transactionTemplate.execute(status -> {
                    for (var spcl : detailsResponseDTO.getSpecializations()) {
                        DoctorSpecializationEntity doctorSpecializationEntityDataToOparate = specializationRepository.findByDoctorIdAndSpecializationId(id,spcl.getSpecializationId()).orElseGet(()->{
                            DoctorSpecializationEntity doctorSpecializationEntityDataToSave 
                                                  =  DoctorSpecializationEntity.builder()
                                                    .status(1)
                                                    .doctorId(id)
                                                    .specializationId(spcl.getSpecializationId())
                                                    .build();
                            return specializationRepository.save(doctorSpecializationEntityDataToSave);
                        });
                  
                    };
                 return null;
               });
            
            },customExecutor);

            CompletableFuture.allOf(dbCheckFutureAbout,dbCheckFutureId,specializations).join();
            return "Details added";
        }catch(Exception e){
            return e.getMessage();
        }
        // finally{
        //     customExecutor.shutdown();
        // }
    }

    @PreDestroy
    public void cleanup() {
        log.info("Shutting down custom executor...");
        customExecutor.shutdown();
    }


    public String updateRegImage(Long id, MultipartFile file){
        DoctorIdEntity data = doctorIdRepository.findById(id).orElseThrow(()->new RuntimeException("no id found   "+id));
        String oldImageRegPath = data.getIdImage();
         if (oldImageRegPath != null && !oldImageRegPath.isEmpty()) {
            try {
                imageRegistrationHelper.deleteFile(oldImageRegPath); 
            } catch (Exception e) {
                System.err.println("Warning: Could not delete old Gov ID: " + e.getMessage());
            }
        }
        try{
            String url = imageRegistrationHelper.saveFile(file);
            data.setRegistrationNoImage(url);
            doctorIdRepository.save(data);
        }catch(Exception e){
            return e.getMessage();
            // log.info(e.getMessage());
        }
        
        return "image uploaded";
    }

     public String updateGovImage(Long id, MultipartFile file){
        DoctorIdEntity data = doctorIdRepository.findById(id).orElseThrow(()->new RuntimeException("no id found   "+id));
        String oldImageRegPath = data.getIdImage();
         if (oldImageRegPath != null && !oldImageRegPath.isEmpty()) {
            try {
                govDocImageUploadHelper.deleteFile(oldImageRegPath); 
            } catch (Exception e) {
                System.err.println("Warning: Could not delete old Gov ID: " + e.getMessage());
            }
        }
        try{
            String url = govDocImageUploadHelper.saveFile(file);
            data.setIdImage(url);
            doctorIdRepository.save(data);
        }catch(Exception e){
            return e.getMessage();
            // log.info(e.getMessage());
        }
        
        return "image uploaded";
    }
}

